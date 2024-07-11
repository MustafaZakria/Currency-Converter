package com.zeko.currencyconverterapp.ui

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.core.view.children
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.databinding.ActivitySettingsBinding
import com.zeko.currencyconverterapp.sharedPref.CurrencySharedPreference
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding : ActivitySettingsBinding

    @Inject
    lateinit var sharedPreference : CurrencySharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSettings.setOnClickListener {
            returnToMain()
        }

        binding.spCurrencies.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

        }
    }

    private fun returnToMain() {
        finish()
    }
}