package com.kdh.eatwd.presenter.ui.home

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.*
import com.kdh.eatwd.databinding.ActivityMainBinding
import com.kdh.eatwd.presenter.ui.custom.CustomCircleProgressBar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btn1.setOnClickListener {
            viewModel.getAirInfo("40", "40")
        }
        setObserver()
    }

    fun setObserver(){
        lifecycleScope.launch{
            repeatOnLifecycle(Lifecycle.State.STARTED){
                viewModel.airInfo.collect{ pollution ->
                    binding.tvPoll.text = pollution?.aqius.toString()
                }
            }
        }
    }
}