package com.zeko.currencyconverterapp.ui

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import com.zeko.currencyconverterapp.databinding.ActivitySettingsBinding
import com.zeko.currencyconverterapp.service.CurrencyService
import com.zeko.currencyconverterapp.sharedPref.CurrencySharedPreference
import com.zeko.currencyconverterapp.util.Constants.INITIAL_DELAY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var sharedPreference: CurrencySharedPreference

    @Inject
    lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivSettings.setOnClickListener {
            returnToMain()
        }

        binding.spCurrencies.apply {
            onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    sharedPreference.putCurrencyToNotify(selectedItem.toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    sharedPreference.putCurrencyToNotify(getItemAtPosition(0).toString())
                }

            }
        }

        val switch = binding.switch1

        if (sharedPreference.isSwitchChecked()) {
            switch.isChecked = true
        }

        switch.setOnCheckedChangeListener { _, isChecked ->
            val pendingIntent = PendingIntent.getService(
                this,
                0,
                Intent(this, CurrencyService::class.java),
                FLAG_IMMUTABLE
            )
            if (isChecked) {
                val fireAt = System.currentTimeMillis() + INITIAL_DELAY.toLong()
                alarmManager.setInexactRepeating(
                    AlarmManager.RTC_WAKEUP,
                    fireAt,
                    AlarmManager.INTERVAL_HALF_DAY,
                    pendingIntent
                )
                Log.d("##", "fire the alarm manager!")
            } else {
                alarmManager.cancel(pendingIntent)
            }
            sharedPreference.setSwitchChecked(isChecked)
        }

    }

    private fun returnToMain() {
        finish()
    }

}