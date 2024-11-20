package com.mvvmproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.mvvmproject.databinding.FragmentDescriptonBinding
import com.mvvmproject.presentation.VideoDetailViewModel
import com.mvvmproject.common.Resource

class DescriptionFragment : Fragment() {
    private var _binding: FragmentDescriptonBinding? = null
    private val binding get() = _binding!!
    private val videoDetailViewModel: VideoDetailViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDescriptonBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handelVideoDetailsRes()
    }

    private fun handelVideoDetailsRes() {
        videoDetailViewModel.videoDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading ->{}

                is Resource.Success -> {
                    it.data?.let { data ->
                        binding.tvDescription.text = data.postDesc
                    }
                }

                is Resource.Error ->{}

                is Resource.InternetError -> {
                }

                else ->{}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        binding.root.requestLayout()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}