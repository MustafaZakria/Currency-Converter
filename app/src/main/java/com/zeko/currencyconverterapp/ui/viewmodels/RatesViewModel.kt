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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val repo: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    init {
//        loadRateValues()
    }

    private val _spinnerValue = MutableLiveData("USD")
    val spinnerValue: LiveData<String> = _spinnerValue

    private val _rateItems = MutableLiveData<MutableList<RateItem>>(mutableListOf())
    val rateItems: LiveData<MutableList<RateItem>> = _rateItems

    private val supportedCurrencies = listOf("EGP", "AUD", "CAD", "EUR", "GBP", "HKD", "RUB", "USD")

    private fun loadRateValues() {
        viewModelScope.launch(dispatchers.io) {
            _rateItems.postValue(mutableListOf())
            when (val ratesResponse = repo.getRates()) {
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val base = ratesResponse.data.base
                    insertRates(rates, base)
                }
                is Resource.Error -> {
                    Log.d("##", "Error in loading rate items")
                }
            }
        }
    }

    private fun insertRates(
        rates: Rates,
        base: String
    ) {
        for (curr in supportedCurrencies) {
            _rateItems.value?.apply {
                add(getRateItem(curr, rates, base))
                _rateItems.postValue(this)
            }
        }
    }

    private fun getRateItem(currency: String, rates: Rates, responseBase: String): RateItem {

        var rate = getRateForCurrency(currency, rates)!!.toDouble()

        _spinnerValue.value?.let {
            if (it != responseBase) {
                val spValue = getRateForCurrency(it, rates)!!
                rate /= spValue.toDouble()
            }
        }

        rate = round(rate * 100) / 100
        return RateItem(currency, rate)
    }

    fun setSpValue(value: String) {
        _spinnerValue.postValue(value)
        loadRateValues()
    }
}