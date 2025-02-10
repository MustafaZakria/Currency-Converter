package com.zeko.currencyconverterapp.worker

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_IMMUTABLE
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.repos.MainRepository
import com.zeko.currencyconverterapp.ui.MainActivity
import com.zeko.currencyconverterapp.util.Constants.BASE
import com.zeko.currencyconverterapp.util.Constants.CHANNEL_ID
import com.zeko.currencyconverterapp.util.Constants.CHANNEL_NAME
import com.zeko.currencyconverterapp.util.Constants.NOTIFICATION_ID
import com.zeko.currencyconverterapp.util.Constants.NOTIFICATION_TITLE
import com.zeko.currencyconverterapp.util.RateItem
import com.zeko.currencyconverterapp.util.Resource
import com.zeko.currencyconverterapp.util.Util.calcCurrencyRate
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException

@HiltWorker
class CurrencyUpdatesWorker @AssistedInject constructor(
    val context: Context,
    val params: WorkerParameters,
    private val repository: MainRepository
) : CoroutineWorker(context, params) {


    override suspend fun doWork(): Result = try {
        Log.d("***", "Here Worker")
        val rates = withContext(Dispatchers.IO) { loadFavRates() }
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val notificationBuilder = createNotification().setContentText(getNotificationText(rates))
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())

        Result.success()
    } catch (e: HttpException) {
        Result.retry()
    }



    private suspend fun loadFavRates(): MutableList<RateItem> {
        val result = mutableListOf<RateItem>()
        when (val rates = repository.getRates()) {
            is Resource.Success -> {
                rates.data?.rates?.let {
                    val favCurrencies =
                        params.inputData.getString(FAV_CURRENCIES)?.split(",") ?: listOf()
                    val base = params.inputData.getString(BASE_CURRENCY) ?: BASE
                    favCurrencies.forEach { curr ->
                        if (curr != base) {
                            val rate = calcCurrencyRate(curr, it, base)
                            result.add(RateItem(curr, rate))
                        }
                    }
                }
            }

            else -> {
                Log.d("##", "Error in loading rate items")
            }
        }
        return result
    }

    private fun createNotificationChannel() {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(
            NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
        )
    }

    private fun createNotification(): NotificationCompat.Builder {
        createNotificationChannel()
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setSmallIcon(R.drawable.notification_icon)
            .setContentIntent(
                PendingIntent.getActivity(
                    context, 2, Intent(context, MainActivity::class.java),
                    FLAG_IMMUTABLE
                )
            )
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

    private fun getNotificationText(rates: MutableList<RateItem>): String {
        var result = ""
        if (rates == emptyList<RateItem>()) {
            result = "Check the Latest currency rates!"
        } else {
            val base = params.inputData.getString(BASE_CURRENCY) ?: BASE
            result += "1 $base = "
            rates.forEach { rate ->
                result += rate.toString()
                if (rate != rates.last()) {
                    result += ", "
                }
            }
            result.drop(2)
        }
        return result
    }

    companion object {
        const val FAV_CURRENCIES = "favourite currencies"
        const val BASE_CURRENCY = "base currency"
    }
}