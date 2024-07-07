package com.zeko.currencyconverterapp.util

import android.graphics.Color
import android.util.Log
import android.view.View
import android.widget.*
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.ui.viewmodels.MainViewModel.CurrencyEvent
import com.zeko.currencyconverterapp.ui.viewmodels.RatesViewModel

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
    this.setImageResource( when (rateItem.getCurrency()) {
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
            viewModel.setSpValue(spinner.selectedItem.toString())
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

@BindingAdapter("setStarImage")
fun ImageView.setStarImage(isFavourite: Boolean) {
    this.setImageResource( when(isFavourite) {
        true -> R.drawable.ic_star_filled
        false -> R.drawable.ic_star_border
    })
}
