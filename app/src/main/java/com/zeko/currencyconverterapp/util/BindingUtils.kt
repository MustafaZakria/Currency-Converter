package com.zeko.currencyconverterapp.util

import android.graphics.Color
import android.media.Image
import android.view.View
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.databinding.adapters.ImageViewBindingAdapter.setImageDrawable
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.ui.viewmodels.MainViewModel.CurrencyEvent
import com.zeko.currencyconverterapp.ui.viewmodels.RatesViewModel
import java.util.*

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
    this.setImageResource( when (rateItem.getCounty()) {
        "EGP" -> R.drawable.egp
        "AUD" -> R.drawable.aud
        "CAD" -> R.drawable.cad
        "EUR" -> R.drawable.eur
        "GBP" -> R.drawable.gbp
        "HKD" -> R.drawable.hkd
        "RUB" -> R.drawable.rub
        "USD" -> R.drawable.usd
        else -> R.drawable.ic_launcher_foreground
    })
}

@BindingAdapter("onItemSelected")
fun setOnItemSelected(spinner: Spinner, viewModel: RatesViewModel) {
    spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            viewModel.setSpinnerValue(spinner.selectedItem.toString())
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}