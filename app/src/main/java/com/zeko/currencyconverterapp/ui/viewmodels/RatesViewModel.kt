package com.zeko.currencyconverterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeko.currencyconverterapp.data.models.Rates
import com.zeko.currencyconverterapp.repos.MainRepository
import com.zeko.currencyconverterapp.util.DispatcherProvider
import com.zeko.currencyconverterapp.util.RateItem
import com.zeko.currencyconverterapp.util.Resource
import com.zeko.currencyconverterapp.util.getRateForCurrency
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val repo: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    init {
        loadRateItems()
    }

    private var spinnerValue = "USD"

    private val _rateItems = MutableLiveData<MutableList<RateItem>>(mutableListOf())
    val rateItems: LiveData<MutableList<RateItem>> = _rateItems

    private val supportedCurrencies = listOf("EGP", "AUD", "CAD", "EUR", "GBP", "HKD", "RUB", "USD")

    private fun loadRateItems() {
        viewModelScope.launch(dispatchers.io) {
            _rateItems.postValue(mutableListOf())
            when (val ratesResponse = repo.getRates()) {
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val base = ratesResponse.data.base
                    for (curr in supportedCurrencies) {
                        _rateItems.value?.apply {
                            add(getRateItem(curr, rates, base))
                            _rateItems.postValue(this)

                        }
                    }
                }
                is Resource.Error -> {
                    Log.d("##", "Error in loading rate items")
                }
            }
        }
    }

    private fun getRateItem(currency: String, rates: Rates, responseBase: String): RateItem {

        var rate = getRateForCurrency(currency, rates)!!.toDouble()

        if (spinnerValue != responseBase) {
            spinnerValue = getRateForCurrency(currency, rates)!!
            rate /= spinnerValue.toDouble()
        }

        return RateItem(currency, rate)
    }

    fun setSpinnerValue(value: String) {
        spinnerValue = value
        loadRateItems()
    }
}