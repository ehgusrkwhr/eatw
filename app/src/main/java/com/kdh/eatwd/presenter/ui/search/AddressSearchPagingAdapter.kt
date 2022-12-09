package com.kdh.eatwd.presenter.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.databinding.ItemAddressInfoBinding

class AddressSearchPagingAdapter : PagingDataAdapter<AddressResponse.Results.Juso, AddressSearchPagingAdapter.AddressSearchPagingViewHolder>(addressDiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressSearchPagingViewHolder {
        val binding = ItemAddressInfoBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return AddressSearchPagingViewHolder(binding)

    }

    override fun onBindViewHolder(holder: AddressSearchPagingViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }

    }
    inner class AddressSearchPagingViewHolder(private val binding : ItemAddressInfoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(juso : AddressResponse.Results.Juso){
            binding.tvJuso.text = juso.roadAddr
        }
    }

    companion object {
        private val addressDiffCallback = object : DiffUtil.ItemCallback<AddressResponse.Results.Juso>() {
            override fun areItemsTheSame(oldItem: AddressResponse.Results.Juso, newItem: AddressResponse.Results.Juso): Boolean {
                return oldItem.admCd == newItem.admCd
            }

            override fun areContentsTheSame(oldItem: AddressResponse.Results.Juso, newItem: AddressResponse.Results.Juso): Boolean {
                return oldItem == newItem
            }

        }
    }


}


