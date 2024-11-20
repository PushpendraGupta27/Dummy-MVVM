package com.mvvmproject.ui.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.mvvmproject.ui.CommentsFragment
import com.mvvmproject.ui.DescriptionFragment

class ViewPager(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
) : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 ->  DescriptionFragment()
            1 ->  CommentsFragment()
            else -> DescriptionFragment()
        }
    }
}