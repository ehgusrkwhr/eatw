package com.kdh.eatwd.presenter.ui.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kdh.eatwd.R
import com.kdh.eatwd.databinding.ActivityMainBinding
import com.kdh.eatwd.presenter.ui.custom.CustomCaptureDialog
import com.kdh.eatwd.presenter.ui.search.AddressSearchBottomSheet
import com.kdh.eatwd.presenter.util.Constants.AFTER_TOMORROW_KEY
import com.kdh.eatwd.presenter.util.Constants.TOMORROW_KEY
import com.kdh.eatwd.presenter.util.LocationUtil
import com.kdh.eatwd.presenter.util.captureScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.io.File


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private val LOCATION_PERMISSON = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val PERMISSION_CODE = 100
    private var requestPermissionLauncher: ActivityResultLauncher<Array<String>>? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null

    lateinit var addressSearchBottomSheet : AddressSearchBottomSheet
    lateinit var locationUtil: LocationUtil

    // 위도와 경도를 저장
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var weatherAdapter: WeatherItemAdapter
    private lateinit var weatherDayItemAdapter: WeatherDayItemAdapter
    private lateinit var weatherDayTitleAdapter: WeatherDayTitleAdapter
    private lateinit var concatAdapter: ConcatAdapter

    private var positionMap = mutableMapOf<String, Int>()



//    private var positionMap: MutableMap<String, Int> by lazy {
//        positionMap.put(TOMORROW_KEY, 0)
//        positionMap.put(AFTER_TOMORROW_KEY, 0)
//    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isPermissions()
        initView()
        setObserver()
        setEvent()
    }


    private fun initView() {
        initAds()
        initProgressBar()
        initLocation()
        setWeatherAdapter()
        onCaptureDialog()
        searchAddress()

    }

    private fun initProgressBar() {
        // progress init
        binding.customProgress.apply {
            progressMax = 200f
            progressBarColor = resources.getColor(R.color.dark_blue)
            progressBarColorStart = resources.getColor(R.color.pink_red)
            progressBarColorEnd = Color.WHITE
            roundBorder = true
            startAngle = 0.0f
            progressBarWidth = 7f // in DP
            backgroundProgressBarWidth = 3f // in DP
//            progress = 0.1f
//            setProgressWithAnimation(0.1f, 4000) // =1s

        }
    }

    private fun initAds() {
        MobileAds.initialize(this)
        binding.adView.loadAd(AdRequest.Builder().build())
    }

    private fun initLocation() {
        //location init
        locationUtil = LocationUtil(this@MainActivity)
        //초기값이면 업데이트
        if (latitude == 0.0 || longitude == 0.0) {
            latitude = locationUtil.getLocationLatitude()
            longitude = locationUtil.getLocationLongitude()
        }

        if (latitude != 0.0 && longitude != 0.0) {
            val address = locationUtil.getCurrentAddress(this, latitude, longitude)
            with(binding) {
                val addressBox = arrayOfNulls<String>(3)
                addressBox[0] = address?.thoroughfare ?: "두두 3동"
                addressBox[1] = address?.countryName ?: "수리남"
                addressBox[2] = address?.adminArea ?: "침산동"
                val result = addressBox[1] + "\t" + addressBox[2] + "\n" + addressBox[0]
                tvAddress.text = result
            }
            initApiCall()
        }
    }

    private fun initApiCall() {
        fetchApiEventData()
    }

    private fun setEvent() {
        with(binding) {
            scrollView.viewTreeObserver.addOnScrollChangedListener {
                layoutRefresh.isEnabled = scrollView.scrollY == 0
            }
            layoutRefresh.setOnRefreshListener {
                //딜레이 3초
                initView()
                layoutRefresh.isRefreshing = false
            }

        }
    }

    private fun fetchApiEventData() {
        viewModel.getAirInfo(latitude, longitude)
        viewModel.fetchWeatherInfo(latitude, longitude)
    }


    private fun setObserver() {

        lifecycleScope.launch {
            viewModel.airInfo.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { pollution ->
                    val pollCount = pollution?.aqius ?: 0
                    binding.tvPoll.text = pollCount.toString()
                    Log.d("dodo55 ", "pollCount.toFloat() : $pollCount.toFloat()")
                    binding.customProgress.progress = pollCount.toFloat()
//                    binding.customProgress.setPollCount(pollCount)

                }
        }

        lifecycleScope.launch {
            viewModel.weatherInfo.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { weatherInfo ->
                    weatherAdapter.submitList(weatherInfo)
                }
        }

        lifecycleScope.launch {
            viewModel.weatherShortInfo.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { weatherShortInfo ->
                    weatherDayItemAdapter.submitList(weatherShortInfo)
                }
        }

        lifecycleScope.launch {
            viewModel.scrollPositionTitle.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect {
                    binding.tvDayWeatherTitle.text = it ?: "오늘"
                }
        }

        lifecycleScope.launch {
            viewModel.loadingFlag.flowWithLifecycle(lifecycle, Lifecycle.State.STARTED)
                .collect { flag ->
                    with(binding) {
                        if (flag) {
                            rvWeatherInfoShimmer.startShimmer()
                            rvWeatherInfoShimmer.visibility = View.VISIBLE
                            rvWeatherInfo.visibility = View.GONE
                        } else {
                            rvWeatherInfoShimmer.stopShimmer()
                            rvWeatherInfoShimmer.visibility = View.GONE
                            rvWeatherInfo.visibility = View.VISIBLE
                        }
                    }
                }
        }

    }

    private fun setWeatherAdapter() {
        positionMap[TOMORROW_KEY] = 999
        positionMap[AFTER_TOMORROW_KEY] = 999
        weatherAdapter = WeatherItemAdapter()
        binding.rvWeatherInfo.adapter = weatherAdapter
        binding.rvWeatherInfo.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        weatherDayItemAdapter = WeatherDayItemAdapter(positionMap) { position ->

            val lastVisibleItemPosition = binding.rvWeatherDayInfo.layoutManager as LinearLayoutManager
            val firstPos = lastVisibleItemPosition.findFirstVisibleItemPosition()

            if (positionMap.isNotEmpty()) {
                if (firstPos >= positionMap[TOMORROW_KEY]!! && firstPos < positionMap[AFTER_TOMORROW_KEY]!!) {
                    viewModel.scrollPositionTitle.value = resources.getString(R.string.weather_tomorrow)
                } else if (firstPos >= positionMap[AFTER_TOMORROW_KEY]!!) {
                    viewModel.scrollPositionTitle.value = resources.getString(R.string.weather_after_tomorrow)
                } else {
                    viewModel.scrollPositionTitle.value = resources.getString(R.string.weather_today)
                }
            }
        }
        binding.rvWeatherDayInfo.adapter = weatherDayItemAdapter

    }

    private fun isPermissions() {
        setPermissionLauncher()
        if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                this@MainActivity,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // 권한요청!!
            requestPermissionLauncher?.launch(LOCATION_PERMISSON)
//            ActivityCompat.requestPermissions(this@MainActivity,LOCATION_PERMISSON,PERMISSION_CODE)
        }
    }

    private fun setPermissionLauncher() {

        requestPermissionLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ) { result: Map<String, Boolean> ->
                val deniedList: List<String> = result.filter {
                    !it.value
                }.map {
                    it.key
                }
                when {
                    deniedList.isNotEmpty() -> {
                        val map = deniedList.groupBy { permission ->
                            if (shouldShowRequestPermissionRationale(permission)) "DENIED" else "EXPLAINED"
                        }
                        map["DENIED"]?.let {
                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                LOCATION_PERMISSON,
                                PERMISSION_CODE
                            )
                        }
                        map["EXPLAINED"]?.let {
                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                LOCATION_PERMISSON,
                                PERMISSION_CODE
                            )
                        }
                    }
                    else -> {
                        Log.d("dodo55 ", "통과");
                    }
                }
            }

    }

    private fun onCaptureDialog() {
        binding.ivShareSns.setOnClickListener {
            val screen = captureScreen()
            val customCaptureDialog = CustomCaptureDialog(this@MainActivity, screen) { fileName ->
                val intent = Intent(Intent.ACTION_SEND)
                val fileUri: Uri? = FileProvider.getUriForFile(this, "com.kdh.eatwd.fileprovider", File(fileName))
                fileUri?.let { uri ->
                    intent.apply {
                        putExtra(Intent.EXTRA_STREAM, uri)
//                        putExtra(Intent.EXTRA_TEXT,"https://play.google.com/store/apps/details?id=blue.soo.rainsounds")
                        addCategory(Intent.CATEGORY_DEFAULT)
                        type = "image/*"
                        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                        startActivity(this)
                    }
                }
            }
            customCaptureDialog.show()
        }
    }

    private fun searchAddress(){
        binding.ivAddressSerach.setOnClickListener {
            addressSearchBottomSheet = AddressSearchBottomSheet()
            addressSearchBottomSheet.show(supportFragmentManager,addressSearchBottomSheet.tag)
//            val bottomSheetView = layoutInflater.inflate(R.layout.dialog_bottom_address_search,null)
//            val dialog = BottomSheetDialog(this)
//            dialog.behavior.state = BottomSheetBehavior.STATE_EXPANDED
//            dialog.setContentView(bottomSheetView)
//            dialog.show()
        }
    }

}