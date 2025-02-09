package com.zeko.currencyconverterapp.ui.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeko.currencyconverterapp.data.models.CurrencyResponse
import com.zeko.currencyconverterapp.repos.MainRepository
import com.zeko.currencyconverterapp.sharedPref.CurrencySharedPreference
import com.zeko.currencyconverterapp.util.Constants.BASE
import com.zeko.currencyconverterapp.util.DispatcherProvider
import com.zeko.currencyconverterapp.util.RateItem
import com.zeko.currencyconverterapp.util.Resource
import com.zeko.currencyconverterapp.util.Util.calcCurrencyRate
import com.zeko.currencyconverterapp.util.Util.isCurrencyFavourite
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.LinkedList
import javax.inject.Inject

@HiltViewModel
class RatesViewModel @Inject constructor(
    private val repo: MainRepository,
    private val dispatchers: DispatcherProvider,
    private val sharedPreference: CurrencySharedPreference
) : ViewModel() {

    private val _spinnerValue = MutableLiveData("USD")
    val spinnerValue: LiveData<String> = _spinnerValue

    private val _rateItems = MediatorLiveData<LinkedList<RateItem>>(LinkedList())
    val rateItems: LiveData<LinkedList<RateItem>> = _rateItems

    private val supportedCurrencies = listOf("EGP", "AUD", "CAD", "EUR", "GBP", "HKD", "RUB", "USD")

    private val currencyResponse = MutableLiveData<CurrencyResponse>()

    init {
        loadRateValues()

        _rateItems.addSource(_spinnerValue) { baseValue ->
            updateRateItems(baseValue, currencyResponse.value)
        }

        // Observe currencyResponse
        _rateItems.addSource(currencyResponse) { response ->
            updateRateItems(_spinnerValue.value, response)
        }
    }

    private fun updateRateItems(baseValue: String?, response: CurrencyResponse?) {
        val base = baseValue ?: BASE
        val linkedList = LinkedList<RateItem>()

        if (response != null) {
            val rates = response.rates
            for (curr in supportedCurrencies) {
                val currencyRate = calcCurrencyRate(curr, rates, base)
                val isFav = isCurrencyFavourite(curr, sharedPreference.getFavCurrencies())
                linkedList.addRateItem(RateItem(curr, currencyRate, isFav))
            }
        }

        _rateItems.postValue(linkedList)
    }

    private fun loadRateValues() {
        viewModelScope.launch(dispatchers.io) {
            when (val ratesResponse = repo.getRates()) {
                is Resource.Success -> {
                    currencyResponse.postValue(ratesResponse.data!!)
                }

                is Resource.Error -> {
                    Log.d("##", "Error in loading rate items")
                }
            }
        }
    }


    fun setSpValue(value: String) {
        _spinnerValue.postValue(value)
    }

    fun addFavRate(rateItem: RateItem) {
        _rateItems.value?.apply {
            remove(rateItem)
            val newRateItem = rateItem.copy(isFavourite = !rateItem.isFavourite)
            addRateItem(newRateItem)
            putToSharedPref(newRateItem)
            _rateItems.postValue(this)
        }
    }


    private fun putToSharedPref(item: RateItem) {
        if (item.isFavourite) {
            sharedPreference.putFavCurrency(item.getCurrency())
        } else {
            sharedPreference.removeFavCurrency(item.getCurrency())
        }
    }

    private fun LinkedList<RateItem>.addRateItem(rateItem: RateItem) {
        if (rateItem.isFavourite) {
            addFirst(rateItem)
        } else {
            addLast(rateItem)
        }
    }
}