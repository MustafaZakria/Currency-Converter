package com.zeko.currencyconverterapp.service

import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.LifecycleService
import com.zeko.currencyconverterapp.repos.MainRepository
import com.zeko.currencyconverterapp.sharedPref.CurrencySharedPreference
import com.zeko.currencyconverterapp.util.Constants.BASE
import com.zeko.currencyconverterapp.util.Constants.NOTIFICATION_ID
import com.zeko.currencyconverterapp.util.DispatcherProvider
import com.zeko.currencyconverterapp.util.RateItem
import com.zeko.currencyconverterapp.util.Resource
import com.zeko.currencyconverterapp.util.Util.calcCurrencyRate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import javax.inject.Inject

@AndroidEntryPoint
class CurrencyService : LifecycleService() {

    @Inject
    lateinit var repo: MainRepository

    @Inject
    lateinit var dispatcher: DispatcherProvider

    @Inject
    lateinit var sharedPreference: CurrencySharedPreference

    @Inject
    lateinit var notificationBuilder: NotificationCompat.Builder

    @Inject
    lateinit var notificationManager: NotificationManagerCompat

    private val job = SupervisorJob()
    private lateinit var scope: CoroutineScope

    override fun onCreate() {
        super.onCreate()
        scope = CoroutineScope(dispatcher.io + job)
        Log.d("##", "on create!")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        Log.d("##", "on start command!")
        scope.launch {
            val rates = loadFavRates()
            notificationBuilder.setContentText(getNotificationText(rates))
            notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
            stopSelf()
        }

        return START_NOT_STICKY;
    }

    private fun getNotificationText(rates: MutableList<RateItem>): String {
        var result = ""
        if (rates == emptyList<RateItem>()) {
            result = "Check the Latest currency rates!"
        } else {
            val base = sharedPreference.getCurrencyToNotify() ?: BASE
            result += "1 $base = "
            rates.forEach { rate ->
                result += rate.toString()
                if (rate != rates.last()) {
                    result += ", "
                }
            }
        }
        return result
    }

    private suspend fun loadFavRates(): MutableList<RateItem> {
        val result = mutableListOf<RateItem>()
        return scope.async {
            when (val rates = repo.getRates()) {
                is Resource.Success -> {
                    rates.data?.rates?.let {
                        val base = sharedPreference.getCurrencyToNotify() ?: BASE
                        sharedPreference.getFavCurrencies()?.forEach { curr ->
                            Log.d("##", curr)
                            if (curr != base) {
                                val rate = calcCurrencyRate(curr, it, base)
                                result.add(RateItem(curr, rate, true))
                            }
                        }
                    }
                }
                else -> {
                    Log.d("##", "Error in loading rate items")
                }
            }
            return@async result
        }.await()
    }

    override fun onDestroy() {
        super.onDestroy()
        job.cancel()
    }

}