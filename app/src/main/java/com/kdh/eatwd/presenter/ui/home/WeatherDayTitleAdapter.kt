package com.kdh.eatwd.presenter.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdh.eatwd.data.entity.Title
import com.kdh.eatwd.databinding.ItemShortDaysTitleBinding

class WeatherDayTitleAdapter :
    ListAdapter<Title, WeatherDayTitleAdapter.WeatherDayTitleHolder>(
        TitleDiffCallback()
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherDayTitleHolder {
        val binding = ItemShortDaysTitleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return WeatherDayTitleHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherDayTitleHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class WeatherDayTitleHolder(private val binding: ItemShortDaysTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(title: Title) {
            binding.tvDaysTitle.text = title.text
//            binding.tvDaysTitle.text = "테스트"
        }
    }
}

class TitleDiffCallback : DiffUtil.ItemCallback<Title>() {
    override fun areItemsTheSame(oldItem: Title, newItem: Title): Boolean {
        return oldItem.text == newItem.text
    }

    override fun areContentsTheSame(oldItem: Title, newItem: Title): Boolean {
        return oldItem == newItem
    }


}