package com.example.oceanit

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.oceanit.View.GraphFragment
import com.example.oceanit.View.MainFragment
import com.example.oceanit.View.SettingFragment

class FragmentAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    private val num_pages= 3

    override fun getItemCount(): Int = num_pages

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> MainFragment()
            1 -> GraphFragment()
            else -> SettingFragment()
        }
    }

}