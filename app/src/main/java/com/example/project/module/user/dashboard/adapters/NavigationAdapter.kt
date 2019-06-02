package com.example.project.module.user.dashboard.adapters

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter

class NavigationAdapter(val items: ArrayList<Fragment>, manager: FragmentManager) : FragmentStatePagerAdapter(manager){
    override fun getItem(p0: Int) = items[p0]

    override fun getCount(): Int = items.size
}
