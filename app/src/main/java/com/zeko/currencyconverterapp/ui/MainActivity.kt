package com.zeko.currencyconverterapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import com.zeko.currencyconverterapp.databinding.ActivityMainBinding
import com.zeko.currencyconverterapp.tablayout.MyPagerAdapter

class MainActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val viewPager = binding.viewPager
        val tabLayout = binding.tbLayout

        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            binding.viewPager.setCurrentItem(tab.position, true)
            tab.text = adapter.getPageTitle(pos)
        }.attach()
    }
}