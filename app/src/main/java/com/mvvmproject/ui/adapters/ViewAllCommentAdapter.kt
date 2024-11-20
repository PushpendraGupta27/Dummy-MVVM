package com.mvvmproject.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mvvmproject.R
import com.mvvmproject.databinding.ItemVideoAllCommentsBinding
import com.mvvmproject.domain.response.Comment
import com.mvvmproject.utils.formatAsTimeAgo
import com.mvvmproject.utils.loadImage
import com.mvvmproject.utils.remove
import com.mvvmproject.utils.show


class ViewAllCommentAdapter(
    private val arrayList: ArrayList<Comment>,
    val onItemClick: (model: Comment) -> Unit,
) :
    RecyclerView.Adapter<ViewAllCommentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemVideoAllCommentsBinding.inflate(
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

    inner class MyViewHolder(private val binding: ItemVideoAllCommentsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: Comment) {
            binding.tvAuthorName.text = data.userName
            binding.tvAuthorComment.text = data.commentText
            val timeAgo = data.postDatetime?.formatAsTimeAgo()
            binding.tvCommentDate.text = timeAgo
            var url = data.profilePic

            if (!url.isNullOrEmpty()) {
                binding.ivAuthorPic.loadImage(url, R.drawable.comment_user_pic)
            } else {
                binding.ivAuthorPic.setImageResource(R.drawable.comment_user_pic)
            }

            if (data.recentReplies == null) {
                binding.replyGroup.remove()
            } else {
                binding.replyGroup.show()
                url = data.recentReplies?.profilePic
                if (!url.isNullOrEmpty()) {
                    binding.ivAuthorPic.loadImage(url, R.drawable.comment_user_pic)
                } else {
                    binding.ivUserPic.setImageResource(R.drawable.comment_user_pic)
                }

                binding.tvUserName.text = data.recentReplies?.userName
                binding.tvUserComment.text = data.recentReplies?.commentText
            }

            binding.tvReply.setOnClickListener {
                onItemClick(data)
            }
        }
    }
}