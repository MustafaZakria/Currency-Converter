package com.zeko.currencyconverterapp.ui.viewmodels

import android.content.Context
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
import com.zeko.currencyconverterapp.util.Util.getRateForCurrency
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
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
            _convertedValue.postValue(CurrencyEvent.Loading())
            when (val ratesResponse = repo.getRates()) {
                is Resource.Error -> {
                    _convertedValue.postValue(CurrencyEvent.Failure(ratesResponse.message!!))
                }
                is Resource.Success -> {
                    val rates = ratesResponse.data!!.rates
                    val rateTo = getRateForCurrency(toCurrency, rates)?.toFloatOrNull()
                    val rateFrom = getRateForCurrency(fromCurrency, rates)?.toFloatOrNull()
                    if (rateTo == null || rateFrom == null) {
                        _convertedValue.postValue(CurrencyEvent.Failure("Unexpected error"))
                    } else {
                        val convertedCurrency = round(amountFrom * (rateTo / rateFrom) * 100) / 100
                        _convertedValue.postValue(CurrencyEvent.Success("$convertedCurrency"))
                    }
                }
            }

        }

    }

}