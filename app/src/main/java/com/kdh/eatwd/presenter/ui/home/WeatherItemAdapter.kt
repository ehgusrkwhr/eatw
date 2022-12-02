package com.kdh.eatwd.presenter.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdh.eatwd.R
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.databinding.ItemDaysInfoBinding
import com.kdh.eatwd.presenter.util.convertKelvinToCelsius
import com.kdh.eatwd.presenter.util.getDayOfWeek

class WeatherItemAdapter :
    ListAdapter<WeatherInfoResponse.WeatherDetail, WeatherItemAdapter.WeatherItemViewHolder>(
        WeatherDiffCallback()
    ) {
    lateinit var mContext: Context

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mContext = recyclerView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherItemViewHolder {
        val binding = ItemDaysInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class WeatherItemViewHolder(private val binding: ItemDaysInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherInfoResponse: WeatherInfoResponse.WeatherDetail) {
            binding.tvDayTitle.text = getDayOfWeek(weatherInfoResponse.dt_txt.split(" ")[0])
            binding.tvMinTemperature.text = convertKelvinToCelsius(weatherInfoResponse.main.temp_min).toString()
            binding.tvMaxTemperature.text = convertKelvinToCelsius(weatherInfoResponse.main.temp_max).toString()
            Glide.with(mContext).load(
                binding.ivDayImage.context.getString(
                    R.string.weather_middle_image_url,
                    weatherInfoResponse.weather[0].icon
                )
            ).into(binding.ivDayImage)
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