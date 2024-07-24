package com.zeko.currencyconverterapp.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zeko.currencyconverterapp.util.Constants.CONVERTER
import com.zeko.currencyconverterapp.util.Constants.RATES

class MyPagerAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private val fragments: Array<Fragment>
) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val tabsName = arrayOf(RATES, CONVERTER)

    fun getPageTitle(position: Int): CharSequence = tabsName[position]

    override fun getItemCount(): Int = tabsName.size

    override fun createFragment(position: Int): Fragment = fragments[position]
}