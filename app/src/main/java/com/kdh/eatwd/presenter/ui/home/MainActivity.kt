package com.kdh.eatwd.presenter.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
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
    lateinit var locationUtil: LocationUtil

    // 위도와 경도를 저장
    var latitude: Double = 0.0
    var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.btn1.setOnClickListener {
//            viewModel.getAirInfo("40", "40")
//        }
        initView()
        isPermissions()
        setObserver()
    }

    private fun initView() {
        locationUtil = LocationUtil(this@MainActivity)
        locationUtil.g
        viewModel.getAirInfo("40", "40")
    }

    private fun setObserver() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.airInfo.collect { pollution ->
                    binding.tvPoll.text = pollution?.aqius.toString()
                }
            }
        }
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