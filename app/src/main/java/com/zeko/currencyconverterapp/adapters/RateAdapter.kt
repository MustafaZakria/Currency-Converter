package com.zeko.currencyconverterapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeko.currencyconverterapp.databinding.ItemRateBinding
import com.zeko.currencyconverterapp.util.RateItem

class RateAdapter(private val clickListener: FavRateClickListener) :
    ListAdapter<RateItem, RateAdapter.ViewHolder>(RateDiffCallBack()) {

    class ViewHolder private constructor(private val binding: ItemRateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RateItem?, clickListener: FavRateClickListener) {
            binding.rateItem = item
            binding.listener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemRateBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

}

class RateDiffCallBack : DiffUtil.ItemCallback<RateItem>() {
    override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem.toString() == newItem.toString() &&
                oldItem.isFavourite == newItem.isFavourite
    }

    override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}

class FavRateClickListener(val clickListener: (currency: String) -> Unit) {
    fun onClick(cur: String) = clickListener(cur)
}