package com.zeko.currencyconverterapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.databinding.ItemRateBinding
import com.zeko.currencyconverterapp.util.RateItem

class RateAdapter : ListAdapter<RateItem, RateAdapter.ViewHolder>(RateDiffCallBack()) {

    class ViewHolder private constructor(private val binding: ItemRateBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: RateItem?) {
            binding.rateItem = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder{
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
        holder.bind(item)
    }

}
class RateDiffCallBack : DiffUtil.ItemCallback<RateItem>() {
    override fun areItemsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem.toString() == newItem.toString()
    }

    override fun areContentsTheSame(oldItem: RateItem, newItem: RateItem): Boolean {
        return oldItem.hashCode() == newItem.hashCode()
    }
}