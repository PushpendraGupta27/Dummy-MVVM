package com.mvvmproject.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvmproject.R
import com.mvvmproject.databinding.ItemBannerBinding
import com.mvvmproject.domain.response.Carousel
import com.mvvmproject.utils.loadImage

class ViewPagerAdapter(
    val onItemClick: (model: Carousel) -> Unit,
) :
    ListAdapter<Carousel,ViewPagerAdapter.MyViewHolder>(BannerCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemBannerBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class MyViewHolder(private val binding: ItemBannerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Carousel) {
            val url = data.thumbnail

            binding.image.loadImage(url, R.drawable.placeholder_big)

            itemView.setOnClickListener {
                onItemClick(data)
            }
        }
    }

    class BannerCallBack: DiffUtil.ItemCallback<Carousel>(){
        override fun areItemsTheSame(oldItem: Carousel, newItem: Carousel): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Carousel, newItem: Carousel): Boolean {
            return oldItem == newItem
        }
    }
}