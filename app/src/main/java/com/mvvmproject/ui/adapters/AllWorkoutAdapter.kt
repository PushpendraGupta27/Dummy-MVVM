package com.mvvmproject.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mvvmproject.databinding.WorkoutsBinding
import com.mvvmproject.domain.response.Workout
import com.mvvmproject.domain.response.WorkoutList
import com.mvvmproject.utils.toTitleCase

class AllWorkoutAdapter(
    val onItemClick: (pos: Int, model: WorkoutList, src: String) -> Unit,
    val workoutItemClicked: (pos: Int, model: Workout, src: String) -> Unit,
) :
    ListAdapter<WorkoutList, AllWorkoutAdapter.MyViewHolder>(WorkoutCallBack()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = WorkoutsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        return holder.bind(getItem(position))
    }

    inner class MyViewHolder(private val binding: WorkoutsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: WorkoutList) {
            binding.tvWorkout.text = data.displayName?.toTitleCase()
            val workoutsList = ArrayList<Workout>()
            val workoutAdapter = InnerWorkoutAdapter { pos, model, src ->
                workoutItemClicked(pos, model, src)
            }
            binding.rvWorkout.adapter = workoutAdapter
            workoutsList.clear()
            workoutsList.addAll(data.workouts)
            workoutAdapter.submitList(workoutsList)  
            binding.tvViewAll.setOnClickListener {
                onItemClick(adapterPosition, data, "viewAll")
            }
        }
    }
}

// DiffUtil
class WorkoutCallBack : DiffUtil.ItemCallback<WorkoutList>() {
    override fun areItemsTheSame(oldItem: WorkoutList, newItem: WorkoutList): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: WorkoutList, newItem: WorkoutList): Boolean {
        return oldItem == newItem
    }
}