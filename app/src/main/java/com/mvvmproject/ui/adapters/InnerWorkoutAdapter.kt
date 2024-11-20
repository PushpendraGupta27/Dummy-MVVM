package com.mvvmproject.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvmproject.R
import com.mvvmproject.databinding.WorkoutsItemBinding
import com.mvvmproject.domain.response.Workout
import com.mvvmproject.utils.capitalizeWords
import com.mvvmproject.utils.loadImage

class InnerWorkoutAdapter(
    val onItemClick: (pos: Int, model: Workout, src: String) -> Unit,
) :
    ListAdapter<Workout, InnerWorkoutAdapter.ViewHolder>(InnerWorkoutCallBack()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): InnerWorkoutAdapter.ViewHolder {
        val binding = WorkoutsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.innerBind(getItem(position))
    }

    inner class ViewHolder(private val binding: WorkoutsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun innerBind(data: Workout) {
            // Bind your data here
            binding.tvPostName.text = data.postName
            val url = data.thumbnail

            binding.ivWorkout.loadImage(url, R.drawable.placeholder_big)

            if (data.favoriteFlag == 0) {
                binding.ivSave.setImageResource(R.drawable.unmarked)
            } else {
                binding.ivSave.setImageResource(R.drawable.marked)
            }

            if (data.duration != null)
                binding.tvTime.text = buildString { append(data.duration).append(" mins") }
            else binding.tvTime.text = ""

            if (data.fitnessLevel.isNullOrEmpty())
                binding.tvWorkoutMode.text = ""
            else binding.tvWorkoutMode.text = data.fitnessLevel?.capitalizeWords()

            binding.ivSave.setOnClickListener {
                onItemClick.invoke(adapterPosition, data, "save")
            }

            itemView.setOnClickListener {
                onItemClick(adapterPosition, data, "root")
            }
        }
    }
}

class InnerWorkoutCallBack : DiffUtil.ItemCallback<Workout>() {
    override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
        return oldItem == newItem
    }
}