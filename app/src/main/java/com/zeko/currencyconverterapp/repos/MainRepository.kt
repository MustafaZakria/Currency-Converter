package com.zeko.currencyconverterapp.repos

import com.zeko.currencyconverterapp.data.models.CurrencyResponse
import com.zeko.currencyconverterapp.util.Resource


// Interface to give a space for testing (since we wouldn't make internet requests)
interface MainRepository {

    suspend fun getRates(): Resource<CurrencyResponse>
}