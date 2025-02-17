package com.mvvmproject.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvmproject.R
import com.mvvmproject.databinding.FragmentHomeBinding
import com.mvvmproject.domain.response.Carousel
import com.mvvmproject.domain.response.Workout
import com.mvvmproject.domain.response.WorkoutList
import com.mvvmproject.presentation.HomeViewModel
import com.mvvmproject.ui.adapters.AllWorkoutAdapter
import com.mvvmproject.ui.adapters.InnerWorkoutAdapter
import com.mvvmproject.ui.adapters.ViewPagerAdapter
import com.mvvmproject.utils.AppConstants
import com.mvvmproject.common.Resource
import com.mvvmproject.utils.remove
import com.mvvmproject.utils.show
import com.mvvmproject.utils.showToast
import com.mvvmproject.utils.toTitleCase
import dagger.hilt.android.AndroidEntryPoint
import java.util.Timer
import java.util.TimerTask

@AndroidEntryPoint
class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPager: ViewPager2
    private val homeViewModel: HomeViewModel by activityViewModels()
    private var carouselList = ArrayList<Carousel>()
    private lateinit var viewPagerAdapter: ViewPagerAdapter
    private var newWorkoutList = ArrayList<Workout>()
    private lateinit var newWorkoutAdapter: InnerWorkoutAdapter
    private var allWorkoutList = ArrayList<WorkoutList>()
    private lateinit var allWorkoutAdapter: AllWorkoutAdapter

    private lateinit var timer: Timer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel.getCarousel(AppConstants.TOKEN)
        homeViewModel.getNewWorkouts(AppConstants.TOKEN)
        homeViewModel.getAllWorkouts(AppConstants.TOKEN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        handleCarouserRes()
        handleNewWorkoutRes()
        handelAllWorkoutRes()
    }

    private fun initViews() = with(binding) {
        viewPager = vpSlider
        viewPagerAdapter = ViewPagerAdapter() { model ->
            viewPagerItemsClick(model)
        }
        viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(tabLayout, viewPager) { _, _ ->
        }.attach()

        // Add the onPageScrollStateChanged callback to the ViewPager
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageScrollStateChanged(state: Int) {
                super.onPageScrollStateChanged(state)
                if (state == ViewPager2.SCROLL_STATE_DRAGGING) {
                    // The user has manually started scrolling, so reset the timer
                    resetTimer()
                }
            }
        })
        startTimer()

        newWorkoutAdapter = InnerWorkoutAdapter { pos, model, src ->
            newWorkoutItemClicked(pos, model, src)
        }
        rvNewWorkout.adapter = newWorkoutAdapter

        allWorkoutAdapter = AllWorkoutAdapter({ pos, model, src ->
            viewAllClick(pos, model, src)
        }, { pos, model, src ->
            workoutClicks(pos, model, src)
        })
        rvAllWorkouts.adapter = allWorkoutAdapter
    }

    private fun viewAllClick(pos: Int, model: WorkoutList, src: String) {
        showToast(requireContext(), "View all click")
    }

    private fun workoutClicks(pos: Int, model: Workout, src: String) {
        showToast(requireContext(), "workouts click")
    }

    private fun newWorkoutItemClicked(pos: Int, model: Workout, src: String) {
        when (src) {
            "root" -> {
                findNavController().navigate(
                    R.id.action_homeFragment_to_videoDetailFragment,
                    bundleOf("postId" to model.postId)
                )
            }
            "save" -> {
                when (model.favoriteFlag) {
                    0 -> {
                        model.favoriteFlag = 1
                    }

                    else -> {
                        model.favoriteFlag = 0
                    }
                }
                newWorkoutAdapter.notifyItemChanged(pos)
            }
        }
    }

    private fun startTimer() {
        timer = Timer()
        val handler = Handler(Looper.getMainLooper())
        timer.schedule(object : TimerTask() {
            override fun run() {
                handler.post {
                    val currentItem = viewPager.currentItem
                    if (currentItem == carouselList.size - 1) {
                        viewPager.setCurrentItem(0, false)
                    } else {
                        viewPager.setCurrentItem(currentItem + 1, true)
                    }
                }
            }
        }, 3000, 3000)
    }

    private fun resetTimer() {
        timer.cancel()
        startTimer()
    }

    private fun viewPagerItemsClick(model: Carousel) {

    }

    private fun handleCarouserRes() {
        homeViewModel.carouselLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    binding.progressBar.remove()
                    binding.homeGroup.show()
                    it.data?.carouselList?.let { data ->
                        carouselList.clear()
                        carouselList.addAll(data)
                        viewPagerAdapter.submitList(carouselList)
                    }
                }

                is Resource.Error -> binding.progressBar.remove()
                is Resource.InternetError -> {
                    binding.progressBar.remove()
                    showToast(requireContext(), getString(R.string.no_internet))
                }

                else -> binding.progressBar.remove()
            }
        }
    }

    private fun handleNewWorkoutRes() {
        homeViewModel.newWorkoutsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    when(it.code){
                        200->{
                            
                        }
                    }
                    binding.progressBar.remove()
                    binding.homeGroup.show()
                    binding.tvNewWorkout.text =
                        it.data?.newWorkouts?.get(0)?.displayName?.toTitleCase()
                    it.data?.newWorkouts?.get(0)?.workouts?.let { data ->
                        newWorkoutList.clear()
                        newWorkoutList.addAll(data)
                        newWorkoutAdapter.submitList(newWorkoutList)
                    }
                }

                is Resource.Error -> binding.progressBar.remove()
                is Resource.InternetError -> {
                    binding.progressBar.remove()
                    showToast(requireContext(), getString(R.string.no_internet))
                }

                else -> binding.progressBar.remove()
            }
        }
    }

    private fun handelAllWorkoutRes() {
        homeViewModel.allWorkoutsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    binding.progressBar.remove()
                    binding.homeGroup.show()
                    it.data?.workoutList?.let { data ->
                        allWorkoutList.clear()
                        allWorkoutList.addAll(data)
                        allWorkoutAdapter.submitList(allWorkoutList)
                    }
                }

                is Resource.Error -> binding.progressBar.remove()
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
        timer.cancel()
    }
}