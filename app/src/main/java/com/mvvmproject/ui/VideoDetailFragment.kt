package com.mvvmproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvmproject.R
import com.mvvmproject.databinding.FragmentVideoDetailBinding
import com.mvvmproject.presentation.VideoDetailViewModel
import com.mvvmproject.ui.adapters.ViewPager
import com.mvvmproject.utils.AppConstants
import com.mvvmproject.common.Resource
import com.mvvmproject.utils.capitalizeWords
import com.mvvmproject.utils.hide
import com.mvvmproject.utils.loadImage
import com.mvvmproject.utils.remove
import com.mvvmproject.utils.show
import com.mvvmproject.utils.showToast
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class VideoDetailFragment : Fragment() {
    private var _binding: FragmentVideoDetailBinding? = null
    private val binding get() = _binding!!
    private var timeZone = ""
    private var postId = -1
    private val videoDetailViewModel: VideoDetailViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.apply {
            postId = getInt("postId", 0)
        }
        val calender = Calendar.getInstance()
        val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.getDefault())
        timeZone = simpleDateFormat.format(calender.time)
        videoDetailViewModel.videoDetailsApi(AppConstants.TOKEN, postId, timeZone)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentVideoDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewpager = binding.viewpager
        val tabLayout = binding.tabLayout

        val adapter = ViewPager(childFragmentManager, lifecycle)
        viewpager.adapter = adapter

        TabLayoutMediator(tabLayout, viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "Description"
                1 -> tab.text = "Comments"
            }
        }.attach()

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> binding.commentGroup.remove()
                    1 -> binding.commentGroup.show()
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                val position = tab?.position
                when (position) {
                    0 -> binding.btnStartWorkout.remove()
                    1 -> binding.btnStartWorkout.show()
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        })
        handelVideoDetailsRes()
    }

    private fun handelVideoDetailsRes() {
        videoDetailViewModel.videoDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading ->
                    binding.progressBar.show()

                is Resource.Success -> {
                    binding.progressBar.remove()
                    binding.scrollerView.show()
                    binding.btnStartWorkout.show()
                    it.data?.let { data ->
                        val url = data.thumbnail
                        binding.ivImg.loadImage(url, R.drawable.placeholder_big)
                        binding.tvVideoTitle.text = data.postName
                        binding.tvTime.text =
                            buildString { append(data.duration).append(" mins") }
                        binding.tvLevel.text = data.fitnessLevel?.capitalizeWords()
                        binding.tvMuscleBuilding.text =
                            data.muscleGroup?.capitalizeWords()
                        binding.tvWorkoutType.text = data.workoutType?.capitalizeWords()

                        if (data.calorieRange == "0") {
                            binding.tvCalories.hide()
                            binding.ivCalories.hide()
                        } else {
                            binding.tvCalories.text = data.calorieRange
                            binding.ivCalories.show()
                        }

                        if (data.totalComments == 0) {
                            binding.tvViewAllComments.text = "No Comments Yet"
                            binding.tvViewAllComments.isClickable = false
                        } else {
                            binding.tvViewAllComments.text =
                                "View All ${data.totalComments} Comments"
                            binding.tvViewAllComments.isClickable = true
                        }
                    }
                }

                is Resource.Error ->
                    binding.progressBar.remove()

                is Resource.InternetError -> {
                    binding.progressBar.remove()
                    showToast(requireContext(), getString(R.string.no_internet))
                }

                else -> binding.progressBar.remove()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}