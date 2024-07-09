package com.zeko.currencyconverterapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import com.google.android.material.tabs.TabLayoutMediator
import com.zeko.currencyconverterapp.databinding.ActivityMainBinding
import com.zeko.currencyconverterapp.adapters.MyPagerAdapter
import com.zeko.currencyconverterapp.ui.fragments.SettingsFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var  binding : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpTabLayout()
        binding.ivSettings.setOnClickListener {
            navigateToSettingsFragment()
        }

    }

    private fun navigateToSettingsFragment() {
//        val settingsFragment = SettingsFragment()
//        supportFragmentManager.beginTransaction().rep
    }

    private fun setUpTabLayout() {
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