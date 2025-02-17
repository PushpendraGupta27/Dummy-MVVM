package com.mvvmproject.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvmproject.databinding.ItemCheckboxBinding
import com.mvvmproject.domain.response.Items
import com.mvvmproject.utils.capitalizeWords

class SearchCheckboxAdapter(
    private val arrayList: ArrayList<Items>,
    val onItemClick: (pos: Int, model: Items, src: String) -> Unit,
) :
    RecyclerView.Adapter<SearchCheckboxAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCheckboxBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(arrayList[position])
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    inner class MyViewHolder(private val binding: ItemCheckboxBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Items) {
            binding.checkbox.text = data.displayName?.capitalizeWords()

            binding.checkbox.isChecked = data.isSelected

            binding.checkbox.setOnClickListener {
                onItemClick(adapterPosition, data, "root")
            }
        }
    }
}