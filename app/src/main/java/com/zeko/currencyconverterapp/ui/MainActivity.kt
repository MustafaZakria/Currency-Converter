package com.zeko.currencyconverterapp.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.zeko.currencyconverterapp.adapters.MyPagerAdapter
import com.zeko.currencyconverterapp.databinding.ActivityMainBinding
import com.zeko.currencyconverterapp.ui.fragments.ConverterFragment
import com.zeko.currencyconverterapp.ui.fragments.RatesFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }

    private fun setUpTabLayout() {
        val viewPager = binding.viewPager
        val tabLayout = binding.tbLayout

        val fragments = arrayOf(RatesFragment(), ConverterFragment())
        val adapter = MyPagerAdapter(supportFragmentManager, lifecycle, fragments)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, pos ->
            binding.viewPager.setCurrentItem(tab.position, true)
            tab.text = adapter.getPageTitle(pos)
        }.attach()
    }


}