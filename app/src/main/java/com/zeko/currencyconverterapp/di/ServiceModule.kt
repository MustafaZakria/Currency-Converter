package com.zeko.currencyconverterapp.di

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.util.Constants.CHANNEL_ID
import com.zeko.currencyconverterapp.util.Constants.CHANNEL_NAME
import com.zeko.currencyconverterapp.util.Constants.NOTIFICATION_TITLE
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ServiceComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ServiceScoped

@Module
@InstallIn(ServiceComponent::class)
object ServiceModule {

    @ServiceScoped
    @Provides
    fun provideNotificationManager(
        @ApplicationContext context: Context
    ): NotificationManagerCompat {
        val notificationManager = NotificationManagerCompat.from(context)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                CHANNEL_NAME,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        return notificationManager
    }

    @ServiceScoped
    @Provides
    fun provideNotificationBuilder(
        @ApplicationContext context: Context
    ): NotificationCompat.Builder {
        return NotificationCompat.Builder(context, CHANNEL_ID)
            .setContentTitle(NOTIFICATION_TITLE)
            .setSmallIcon(R.drawable.notification_icon)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
    }

}
