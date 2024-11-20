package com.mvvmproject.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.mvvmproject.databinding.FragmentCommentsBinding
import com.mvvmproject.domain.response.Comment
import com.mvvmproject.presentation.VideoDetailViewModel
import com.mvvmproject.ui.adapters.ViewAllCommentAdapter
import com.mvvmproject.common.Resource
import com.mvvmproject.utils.remove
import com.mvvmproject.utils.show

class CommentsFragment : Fragment() {
    private var _binding: FragmentCommentsBinding? = null
    private val binding get() = _binding!!
    private val videoDetailViewModel: VideoDetailViewModel by activityViewModels()
    private var commentsArrayList = ArrayList<Comment>()
    private lateinit var commentAdapter: ViewAllCommentAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentCommentsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handelVideoDetailsRes()
        setAdapter()
    }

    private fun setAdapter() {
        commentAdapter = ViewAllCommentAdapter(commentsArrayList) { model ->
            replyOnClick(model)
        }
        binding.rvComments.adapter = commentAdapter
    }

    private fun replyOnClick(model: Comment) {

    }

    private fun handelVideoDetailsRes() {
        videoDetailViewModel.videoDetailsLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading ->{}

                is Resource.Success -> {
                    it.data?.let { data ->
                        commentsArrayList.clear()
                        commentsArrayList.addAll(data.comments)
                        if (data.comments.isEmpty()) {
                            binding.client.show()
                            binding.rvComments.remove()
                        } else {
                            binding.client.remove()
                            binding.rvComments.show()
                        }
                        commentAdapter.notifyDataSetChanged()
                    }
                }

                is Resource.Error ->{}

                is Resource.InternetError -> {
                }

                else ->{}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}