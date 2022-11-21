package com.kdh.eatwd.presenter.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.databinding.ItemDaysInfoBinding

class WeatherItemAdapter :
    ListAdapter<WeatherInfoResponse.WeatherDetail, WeatherItemAdapter.WeatherItemViewHolder>(WeatherDiffCallback()) {
    lateinit var mContext: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        Log.d("dodo55 ","onAttachedToRecyclerView")
        mContext = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        Log.d("dodo55 ","onCreateViewHolder")
        val binding =
            ItemDaysInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        Log.d("dodo55 ","onBindViewHolder")
        holder.bind(getItem(position))
    }

    inner class WeatherItemViewHolder(private val binding: ItemDaysInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherInfoResponse: WeatherInfoResponse.WeatherDetail) {
//            weatherInfoResponse.dt_txt
            Log.d("dodo55 ","WeatherItemViewHolder.dt_txt ${weatherInfoResponse.dt_txt}")
            binding.tvDayTitle.text = weatherInfoResponse.dt_txt
            binding.tvMinTemperature.text = weatherInfoResponse.main.temp_min.toString()
            binding.tvMaxTemperature.text =weatherInfoResponse.main.temp_max.toString()
            Glide.with(mContext).load("http://openweathermap.org/img/wn/10d@2x.png").into(binding.ivDayImage)
//            binding.pbAvgTemperature = ""

        }
    }
}

class WeatherDiffCallback : DiffUtil.ItemCallback<WeatherInfoResponse.WeatherDetail>() {

    override fun areItemsTheSame(
        oldItem: WeatherInfoResponse.WeatherDetail,
        newItem: WeatherInfoResponse.WeatherDetail
    ): Boolean {
        return oldItem.dt_txt == newItem.dt_txt
    }

    override fun areContentsTheSame(
        oldItem: WeatherInfoResponse.WeatherDetail,
        newItem: WeatherInfoResponse.WeatherDetail
    ): Boolean {
        return oldItem == newItem
    }

}