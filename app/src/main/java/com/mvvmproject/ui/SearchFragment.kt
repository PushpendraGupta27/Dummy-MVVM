package com.mvvmproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.mvvmproject.R
import com.mvvmproject.common.Resource
import com.mvvmproject.databinding.FragmentSearchBinding
import com.mvvmproject.domain.response.Items
import com.mvvmproject.presentation.SearchViewModel
import com.mvvmproject.ui.adapters.FitnessLevelAdapter
import com.mvvmproject.ui.adapters.SearchCheckboxAdapter
import com.mvvmproject.utils.AppConstants
import com.mvvmproject.utils.hide
import com.mvvmproject.utils.remove
import com.mvvmproject.utils.show
import com.mvvmproject.utils.showToast

class SearchFragment : Fragment() {
    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by activityViewModels()
    private lateinit var fitnessLevelAdapter: FitnessLevelAdapter
    private var fitnessLevelList = ArrayList<Items>()
    private lateinit var muscleGroupAdapter: FitnessLevelAdapter
    private var muscleGroupList = ArrayList<Items>()
    private lateinit var equipmentAdapter: SearchCheckboxAdapter
    private var equipmentList = ArrayList<Items>()
    private lateinit var workoutTypeAdapter: SearchCheckboxAdapter
    private var workoutTypeList = ArrayList<Items>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        searchViewModel.searchAttributeApi(AppConstants.TOKEN)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handleSearchAttributeRes()
        setAdapter()
    }

    private fun setAdapter() {
        fitnessLevelAdapter =
            FitnessLevelAdapter(requireContext()) { pos, model, src ->
                fitnessLevelItemClick(pos, model, src)
            }

        var layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvFitnessLevel.layoutManager = layoutManager
        binding.rvFitnessLevel.adapter = fitnessLevelAdapter

        muscleGroupAdapter =
            FitnessLevelAdapter(requireContext()) { pos, model, src ->
                muscleGroupItemClick(pos, model, src)
            }
        layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.rvMuscleGroup.layoutManager = layoutManager
        binding.rvMuscleGroup.adapter = muscleGroupAdapter

        equipmentAdapter =
            SearchCheckboxAdapter(equipmentList) { pos, model, src ->
                equipmentItemClick(pos, model, src)
            }
        binding.rvEquipment.adapter = equipmentAdapter

        workoutTypeAdapter =
            SearchCheckboxAdapter(workoutTypeList) { pos, model, src ->
                workoutTypeItemClick(pos, model, src)
            }
        binding.rvWorkoutType.adapter = workoutTypeAdapter
    }

    private fun fitnessLevelItemClick(pos: Int, model: Items, src: String) {
        when (src) {
            "root" -> {
                // Toggle selection by creating a new list
                fitnessLevelList = fitnessLevelList.mapIndexed { index, item ->
                    if (index == pos) item.copy(isSelected = !item.isSelected) else item
                } as ArrayList<Items>

                // Submit the new list
                fitnessLevelAdapter.submitList(fitnessLevelList.toList())
            }
            /*if (model.isSelected) {
                model.isSelected = false
            } else {
                model.isSelected = true
            }
            fitnessLevelAdapter.submitList(fitnessLevelList)*/
        }
    }

    private fun workoutTypeItemClick(pos: Int, model: Items, src: String) {

    }

    private fun equipmentItemClick(pos: Int, model: Items, src: String) {
    }

    private fun muscleGroupItemClick(pos: Int, model: Items, src: String) {

    }

    private fun handleSearchAttributeRes() {
        searchViewModel.searchAttributeLiveData.observe(viewLifecycleOwner) { it ->
            when (it) {
                is Resource.Loading -> binding.progressBar.show()
                is Resource.Success -> {
                    when (it.code) {
                        200 -> {
                            binding.progressBar.hide()
                            binding.searchViewGroup.show()

                            val fitnessList = it.data?.fitnessLevel?.list
                            fitnessList?.let {
                                if (fitnessLevelList.isEmpty()) {
                                    fitnessLevelList.addAll(it)
                                    fitnessLevelAdapter.submitList(fitnessLevelList)
                                }
                            }

                            val equipmentType = it.data?.equipmentType?.list
                            equipmentType?.let {
                                if (equipmentList.isEmpty()) {
                                    equipmentList.addAll(it)
                                    equipmentAdapter.notifyDataSetChanged()
                                }
                            }
                            val muscleGroup = it.data?.muscleGroup?.list
                            muscleGroup?.let {
                                if (muscleGroupList.isEmpty()) {
                                    muscleGroupList.addAll(it)
                                    muscleGroupAdapter.submitList(muscleGroupList)
                                }
                            }
                            val workoutType = it.data?.workoutType?.list
                            workoutType?.let {
                                if (workoutTypeList.isEmpty()) {
                                    workoutTypeList.addAll(it)
                                    workoutTypeAdapter.notifyDataSetChanged()
                                }
                            }
                        }
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
}