package com.zeko.currencyconverterapp.ui

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.zeko.currencyconverterapp.databinding.ActivitySettingsBinding
import com.zeko.currencyconverterapp.sharedPref.CurrencySharedPreference
import com.zeko.currencyconverterapp.util.Constants.UNIQUE_WORK_NAME
import com.zeko.currencyconverterapp.worker.CurrencyUpdatesWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySettingsBinding

    @Inject
    lateinit var sharedPreference: CurrencySharedPreference


    lateinit var workerManager: WorkManager

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
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

        val launcher = registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                launchWorker()
            } else {
                Toast.makeText(
                    this,
                    "Access Denied!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        val switch = binding.switch1

        if (sharedPreference.isSwitchChecked()) {
            switch.isChecked = true
        }

        workerManager = WorkManager.getInstance(this)

        switch.setOnCheckedChangeListener { _, isChecked ->

            if (isChecked) {
                if (ActivityCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.POST_NOTIFICATIONS
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    launchWorker()
                } else {
                    launcher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                }
            } else {
                workerManager.cancelAllWork()
            }

            sharedPreference.setSwitchChecked(isChecked)
        }

    }

    private fun launchWorker() {
        Log.d("***", "Here")
        val constraints = Constraints.Builder()
            .build()

        val request = PeriodicWorkRequestBuilder<CurrencyUpdatesWorker>(
            15,
            TimeUnit.MINUTES
        )
            .setInputData(
                workDataOf(
                    CurrencyUpdatesWorker.FAV_CURRENCIES to sharedPreference.getFavCurrencies()
                        ?.joinToString(","),
                    CurrencyUpdatesWorker.BASE_CURRENCY to sharedPreference.getCurrencyToNotify()
                )
            )
            .setConstraints(constraints)
            .build()
        Log.d("***", "Here2")
        workerManager.enqueueUniquePeriodicWork(
            UNIQUE_WORK_NAME,
            ExistingPeriodicWorkPolicy.REPLACE,
            request
        ).also { operation ->
            operation.result.addListener(
                { Log.d("***", "Worker enqueued: ${operation.state}") },
                { Runnable::run }
            )
        }
    }

    private fun returnToMain() {
        finish()
    }

}