package com.zeko.currencyconverterapp.ui.fragments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zeko.currencyconverterapp.data.models.Rates
import com.zeko.currencyconverterapp.repos.MainRepository
import com.zeko.currencyconverterapp.util.DispatcherProvider
import com.zeko.currencyconverterapp.util.RateItem
import com.zeko.currencyconverterapp.util.Resource
import kotlinx.coroutines.launch
import javax.inject.Inject

class RatesViewModel @Inject constructor(
    private val repo: MainRepository,
    private val dispatchers: DispatcherProvider
) : ViewModel() {

    private val _rates = MutableLiveData<RateItem>(null)
    val rates: LiveData<RateItem> = _rates

    init {
        loadRates()
    }

    private fun loadRates() {
        viewModelScope.launch(dispatchers.io) {
            when(val ratesResponse = repo.getRates()) {
                is Resource.Success -> {
                    insertData(ratesResponse.data!!.rates, ratesResponse.data.base)
                }
                else -> Unit
            }
        }
    }

    private fun insertData(rates: Rates, base: String) {

    }
}