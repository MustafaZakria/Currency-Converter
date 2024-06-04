package com.zeko.currencyconverterapp.util

import android.graphics.Color
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.zeko.currencyconverterapp.main.MainViewModel.CurrencyEvent

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