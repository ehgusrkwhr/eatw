package com.kdh.eatwd.presenter.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.kdh.eatwd.R
import com.kdh.eatwd.data.entity.WeatherInfoResponse
import com.kdh.eatwd.databinding.ItemShortDaysInfoBinding
import com.kdh.eatwd.presenter.util.Constants.AFTER_TOMORROW
import com.kdh.eatwd.presenter.util.Constants.AFTER_TOMORROW_KEY
import com.kdh.eatwd.presenter.util.Constants.TOMORROW
import com.kdh.eatwd.presenter.util.Constants.TOMORROW_KEY
import com.kdh.eatwd.presenter.util.convertKelvinToCelsius
import com.kdh.eatwd.presenter.util.getTimeOfDay

class WeatherDayItemAdapter(
    val positionMap: MutableMap<String, Int>,
    val onDividePosition: (position: Int) -> Unit
) :
    ListAdapter<WeatherInfoResponse.WeatherDetail, WeatherDayItemAdapter.WeatherDayViewHolder>(
        WeatherDayDiffCallback()
    ) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayViewHolder {
        val binding = ItemShortDaysInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherDayViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherDayViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }


    inner class WeatherDayViewHolder(private val binding: ItemShortDaysInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(weatherInfoResponse: WeatherInfoResponse.WeatherDetail, position: Int) {

            // TODO 날짜바뀌면 뷰홀더 바꾸고 3일 이내만 출력
            // ViewType 어케나눔??


            when (weatherInfoResponse.day_flag) {
                TOMORROW -> {
                    setDivideView(binding, position, TOMORROW_KEY)
                    binding.tvDivideMiddle.text = binding.tvDivideMiddle.context.getString(R.string.weather_tomorrow)
                }
                AFTER_TOMORROW -> {
                    setDivideView(binding, position, AFTER_TOMORROW_KEY)
                    binding.tvDivideMiddle.text = binding.tvDivideMiddle.context.getString(R.string.weather_after_tomorrow)
                }
                else -> {
                    binding.layoutDivideDay.visibility = View.GONE
                }
            }

            binding.tvDayTime.text = getTimeOfDay(weatherInfoResponse.dt_txt.split(" ")[1])
            binding.tvDayTemp.text = convertKelvinToCelsius(weatherInfoResponse.main.temp).toString()
            Glide.with(binding.ivWeatherImage.context).load(
                binding.ivWeatherImage.context.getString(
                    R.string.weather_middle_image_url,
                    weatherInfoResponse.weather[0].icon
                )
            ).into(binding.ivWeatherImage)

            onDividePosition(position)
        }
    }


    private fun setDivideView(binding: ItemShortDaysInfoBinding, position: Int, key: String) {
        positionMap[key] = position
        binding.layoutDivideDay.visibility = View.VISIBLE
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