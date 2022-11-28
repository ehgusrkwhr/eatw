package com.kdh.eatwd.presenter.ui.home

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdh.eatwd.R
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.databinding.ItemShortDaysInfoBinding
import com.kdh.eatwd.presenter.util.convertKelvinToCelsius
import com.kdh.eatwd.presenter.util.getTimeOfDay

class WeatherDayItemAdapter :
    ListAdapter<WeatherInfoResponse.WeatherDetail, WeatherDayItemAdapter.WeatherDayViewHolder>(
        WeatherDayDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayViewHolder {
        val binding = ItemShortDaysInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherDayViewHolder, position: Int) {
        holder.bind(getItem(position))
    }


    inner class WeatherDayViewHolder(private val binding: ItemShortDaysInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherInfoResponse: WeatherInfoResponse.WeatherDetail) {
//            if(dayChanged.isEmpty() || dayChanged != weatherInfoResponse.dt_txt.split(" ")[0]){
//                dayChanged = weatherInfoResponse.dt_txt.split(" ")[0]
//            }
            binding.tvDayTime.text = getTimeOfDay(weatherInfoResponse.dt_txt.split(" ")[1])
            binding.tvDayTemp.text = convertKelvinToCelsius(weatherInfoResponse.main.temp).toString()
            Log.d("dodo55 ", "weatherInfoResponse.weather[0].icon : ${weatherInfoResponse.weather[0].icon}")
            binding.ivWeatherImage
            Glide.with(binding.ivWeatherImage.context).load(
                binding.ivWeatherImage.context.getString(
                    R.string.weather_middle_image_url,
                    weatherInfoResponse.weather[0].icon
                )
            )
                .into(binding.ivWeatherImage)
        }
    }
}

class WeatherDayDiffCallback : DiffUtil.ItemCallback<WeatherInfoResponse.WeatherDetail>() {
    override fun areItemsTheSame(
        oldItem: WeatherInfoResponse.WeatherDetail,
        newItem: WeatherInfoResponse.WeatherDetail
    ) = oldItem.main.temp == newItem.main.temp


    override fun areContentsTheSame(
        oldItem: WeatherInfoResponse.WeatherDetail,
        newItem: WeatherInfoResponse.WeatherDetail
    ) = oldItem == newItem

}