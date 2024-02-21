package com.zeko.currencyconverterapp.tablayout

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.zeko.currencyconverterapp.fragments.ConverterFragment
import com.zeko.currencyconverterapp.fragments.RatesFragment

class MyPagerAdapter (fa: FragmentActivity) : FragmentStateAdapter(fa) {

    fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "converter"
            else -> "rates"

        }
    }

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                ConverterFragment()
            }
            else -> {
                return RatesFragment()
            }
        }
    }
}