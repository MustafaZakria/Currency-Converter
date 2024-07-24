package com.zeko.currencyconverterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeko.currencyconverterapp.repos.MainRepository
import com.zeko.currencyconverterapp.sharedPref.CurrencySharedPreference
import com.zeko.currencyconverterapp.util.*
import com.zeko.currencyconverterapp.util.Constants.BASE
import com.zeko.currencyconverterapp.util.Util.calcCurrencyRate
import com.zeko.currencyconverterapp.util.Util.isCurrencyFavourite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val repo: MainRepository,
    private val dispatchers: DispatcherProvider,
    private val sharedPreference: CurrencySharedPreference
) : ViewModel() {

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
                    for (curr in supportedCurrencies) {
                        _rateItems.value?.apply {
                            val spValue = _spinnerValue.value ?: BASE
                            val currencyRate = calcCurrencyRate(curr, rates, spValue)
                            val isFav = isCurrencyFavourite(curr, sharedPreference)
                            add(RateItem(curr, currencyRate, isFav))
                            _rateItems.postValue(this)
                        }
                    }
                    orderRates()
                }
                is Resource.Error -> {
                    Log.d("##", "Error in loading rate items")
                }
            }
        }
    }

    private fun orderRates() {
        _rateItems.value?.apply {
            val (fav, rest) = this.partition { it.isFavourite }
            this.clear()
            this.addAll(fav)
            this.addAll(rest)
            _rateItems.postValue(this)
        }
    }


    fun setSpValue(value: String) {
        _spinnerValue.postValue(value)
        loadRateValues()
    }

    fun addFavRate(currency: String) {
        _rateItems.value = _rateItems.value?.toMutableList()?.apply {
            for (item in this) {
                if (item.getCurrency() == currency) {
                    val index = this.indexOf(item)
                    this[index] = item.copy(isFavourite = !item.isFavourite)
                    putToSharedPref(this[index])
                    break
                }
            }
        }
        orderRates()
    }


    private fun putToSharedPref(item: RateItem) {
        if (item.isFavourite) {
            sharedPreference.putFavCurrency(item.getCurrency())
        } else {
            sharedPreference.removeFavCurrency(item.getCurrency())
        }
    }
}