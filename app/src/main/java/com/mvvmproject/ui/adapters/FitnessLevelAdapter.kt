package com.mvvmproject.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvmproject.R
import com.mvvmproject.databinding.CustomChipBinding
import com.mvvmproject.domain.response.Items
import com.mvvmproject.utils.capitalizeWords

class FitnessLevelAdapter(
    private var context: Context,
    val onItemClick: (pos: Int, model: Items, src: String) -> Unit,
) :
    ListAdapter<Items, FitnessLevelAdapter.MyViewHolder>(CallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CustomChipBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class MyViewHolder(private val binding: CustomChipBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Items) {
            binding.tvChip.text = data.displayName?.capitalizeWords()

            when {
                data.isSelected -> {
                    binding.tvChip.setBackgroundResource(R.drawable.chip_bg)
                    binding.tvChip.setTextColor(ContextCompat.getColor(context, R.color.white))
                }
                else -> {
                    binding.tvChip.setBackgroundResource(R.drawable.chip_border)
                    binding.tvChip.setTextColor(ContextCompat.getColor(context, R.color.black_txt))
                }
            }

            itemView.setOnClickListener {
                onItemClick(adapterPosition, data, "root")
            }
        }
    }
}

class CallBack : DiffUtil.ItemCallback<Items>() {
    override fun areItemsTheSame(oldItem: Items, newItem: Items): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Items, newItem: Items): Boolean {
        return oldItem == newItem
    }
}