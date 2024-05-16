package com.zeko.currencyconverterapp.tablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zeko.currencyconverterapp.ui.fragments.ConverterFragment
import com.zeko.currencyconverterapp.ui.fragments.RatesFragment
import com.zeko.currencyconverterapp.util.Constants.CONVERTER
import com.zeko.currencyconverterapp.util.Constants.NUM_TABS
import com.zeko.currencyconverterapp.util.Constants.RATES

class MyPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    private val tabsName = arrayOf(CONVERTER, RATES)

    fun getPageTitle(position: Int): CharSequence {
        return tabsName[position]
    }

    override fun getItemCount(): Int = NUM_TABS

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ConverterFragment()
            }
            else -> {
                RatesFragment()
            }
        }
    }
}