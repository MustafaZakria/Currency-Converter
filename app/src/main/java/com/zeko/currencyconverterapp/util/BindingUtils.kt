package com.zeko.currencyconverterapp.util

import android.graphics.Color
import android.media.Image
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.ui.viewmodels.MainViewModel.CurrencyEvent

@BindingAdapter("progressBarStatus")
fun ProgressBar.setProgressStatus(event: CurrencyEvent) {
    isVisible = when(event) {
        is CurrencyEvent.Loading -> true
        else -> false
    }
}

@BindingAdapter("textResult")
fun TextView.setResult(event: CurrencyEvent) {
    when(event) {
        is CurrencyEvent.Success -> {
            text = event.resultText
            setTextColor(Color.BLACK)
        }
        is CurrencyEvent.Failure -> {
            text = event.errorText
            setTextColor(Color.RED)
        }
        else -> Unit
    }
}

@BindingAdapter("currencyImage")
fun ImageView.setCurrencyImage(rateItem: RateItem) {
    when (rateItem.getCounty()) {
        "EGP" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.egp))
        "AUD" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.aud))
        "CAD" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.cad))
        "EUR" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.eur))
        "GBP" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.gbp))
        "HKD" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.hkd))
        "RUB" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.rub))
        "USD" -> this.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.usd))
    }
}