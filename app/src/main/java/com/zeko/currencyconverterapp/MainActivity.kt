package com.zeko.currencyconverterapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.zeko.currencyconverterapp.databinding.ActivityMainBinding
import com.zeko.currencyconverterapp.tablayout.MyPagerAdapter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val fragmentAdapter = MyPagerAdapter(this)
        binding.viewPager.adapter = fragmentAdapter

        TabLayoutMediator(binding.tbLayout, binding.viewPager) { tab, pos ->
//            binding.viewPager.setCurrentItem(tab.position, true)
            tab.text = fragmentAdapter.getPageTitle(pos)
        }.attach()
    }
}