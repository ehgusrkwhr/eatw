package com.kdh.eatwd.presenter.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.kdh.eatwd.databinding.ActivityMainBinding
import com.kdh.eatwd.presenter.util.LocationUtil
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


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
    lateinit var locationUtil: LocationUtil

    // 위도와 경도를 저장
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    private lateinit var weatherAdapter: WeatherItemAdapter

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        isPermissions()
        setWeatherAdapter()
        initView()
        setObserver()
        setEvent()
    }


    private fun initView() {
        locationUtil = LocationUtil(this@MainActivity)
        //초기값이면 업데이트
        if (latitude == 0.0 || longitude == 0.0) {
            latitude = locationUtil.getLocationLatitude()
            longitude = locationUtil.getLocationLongitude()
        }

        if (latitude != 0.0 && longitude != 0.0) {
            val address = locationUtil.getCurrentAddress(latitude, longitude)
            with(binding) {
                val addressBox = arrayOfNulls<String>(3)
                addressBox[0] = address?.thoroughfare ?: "두두 3동"
                addressBox[1] = address?.countryName ?: "수리남"
                addressBox[2] = address?.adminArea ?: "침산동"
                val result = addressBox[1] + "\t" + addressBox[2] + "\n" + addressBox[0]
                tvAddress.text = result
            }
//            initApiCall()
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

        initApiCall()
        lifecycleScope.launch {
            viewModel.airInfo.flowWithLifecycle(lifecycle,Lifecycle.State.STARTED).collect{ pollution ->
                val pollCount = pollution?.aqius ?: 0
                binding.tvPoll.text = pollCount.toString()
                binding.customProgress.setPollCount(pollCount)
            }
        }

        lifecycleScope.launch {
            viewModel.weatherInfo.flowWithLifecycle(lifecycle,Lifecycle.State.STARTED).collect{ weatherInfo ->
                weatherAdapter.submitList(weatherInfo)
            }
        }




//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.airInfo.collect { pollution ->
//                    val pollCount = pollution?.aqius ?: 0
//                    binding.tvPoll.text = pollCount.toString()
//                    binding.customProgress.setPollCount(pollCount)
//                }
//            }
//
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                viewModel.weatherInfo.collect { weatherInfo ->
//                    weatherAdapter.submitList(weatherInfo)
//                }
//            }
//        }
    }

    private fun setWeatherAdapter() {
        weatherAdapter = WeatherItemAdapter()
        binding.rvWeatherInfo.adapter = weatherAdapter
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
                            Log.d("dodo55 ", "한번거부");
                            ActivityCompat.requestPermissions(
                                this@MainActivity,
                                LOCATION_PERMISSON,
                                PERMISSION_CODE
                            )
                        }
                        map["EXPLAINED"]?.let {
                            Log.d("dodo55 ", "두번거부");
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


}