package com.kdh.eatwd.presenter.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kdh.eatwd.data.entity.AddressResponse
import com.kdh.eatwd.databinding.ItemAddressInfoBinding

class AddressSearchAdapter : ListAdapter<AddressResponse.Results.Juso, AddressSearchAdapter.AddressSearchViewHolder>(AddressSearchDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressSearchViewHolder {
        val binding = ItemAddressInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AddressSearchViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddressSearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class AddressSearchViewHolder(private val binding: ItemAddressInfoBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(addressResponse: AddressResponse.Results.Juso) {
            binding.tvJuso.text = addressResponse.roadAddr
        }
    }

}

class AddressSearchDiffCallback : DiffUtil.ItemCallback<AddressResponse.Results.Juso>() {
    override fun areItemsTheSame(oldItem: AddressResponse.Results.Juso, newItem: AddressResponse.Results.Juso): Boolean {
        return oldItem.admCd == newItem.admCd
    }

    override fun areContentsTheSame(oldItem: AddressResponse.Results.Juso, newItem: AddressResponse.Results.Juso): Boolean {
        return oldItem == newItem
    }

}