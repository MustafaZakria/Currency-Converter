package com.zeko.currencyconverterapp.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeko.currencyconverterapp.data.models.Rates
import com.zeko.currencyconverterapp.util.DispatcherProvider
import com.zeko.currencyconverterapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.round

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repo: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    sealed class CurrencyEvent() {
        class Success(val resultText: String) : CurrencyEvent()
        class Failure(val errorText: String) : CurrencyEvent()
        class Loading : CurrencyEvent()
        class Empty : CurrencyEvent()
    }

    private val _convertedValue = MutableLiveData<CurrencyEvent>(CurrencyEvent.Empty())
    val convertedValue: LiveData<CurrencyEvent> = _convertedValue

    fun convert(
        amount: String,
        fromCurrency: String,
        toCurrency: String
    ) {
        val amountFrom = amount.toFloatOrNull()
        if (amountFrom == null) {
            _convertedValue.value = CurrencyEvent.Failure("Unexpected error")
            return
        }
        viewModelScope.launch(dispatchers.io) {
            _convertedValue.value = CurrencyEvent.Loading()
            when (val ratesResponse = repo.getRates(fromCurrency)) {
                is Resource.Error -> _convertedValue.value =
                    CurrencyEvent.Failure(ratesResponse.message!!)
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rate = getRateForCurrency(toCurrency, rates)?.toFloatOrNull()
                    if (rate == null) {
                        _convertedValue.value = CurrencyEvent.Failure("Unexpected error")
                    } else {
                        val convertedCurrency = round(amountFrom * rate * 100) / 100
                        _convertedValue.value = CurrencyEvent.Success("$convertedCurrency")
                    }
                }
            }

        }

    }

    fun getRateForCurrency(currency: String, rates: Rates): String? {
        return when (currency) {
            "CAD" -> rates.CAD
            "HKD" -> rates.HKD
            "ISK" -> rates.ISK
            "EUR" -> rates.EUR
            "PHP" -> rates.PHP
            "DKK" -> rates.DKK
            "HUF" -> rates.HUF
            "CZK" -> rates.CZK
            "AUD" -> rates.AUD
            "RON" -> rates.RON
            "SEK" -> rates.SEK
            "IDR" -> rates.IDR
            "INR" -> rates.INR
            "BRL" -> rates.BRL
            "RUB" -> rates.RUB
            "HRK" -> rates.HRK
            "JPY" -> rates.JPY
            "THB" -> rates.THB
            "CHF" -> rates.CHF
            "SGD" -> rates.SGD
            "PLN" -> rates.PLN
            "BGN" -> rates.BGN
            "CNY" -> rates.CNY
            "NOK" -> rates.NOK
            "NZD" -> rates.NZD
            "ZAR" -> rates.ZAR
            "USD" -> rates.USD
            "MXN" -> rates.MXN
            "ILS" -> rates.ILS
            "GBP" -> rates.GBP
            else -> null
        }
    }

}