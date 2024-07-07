package com.zeko.currencyconverterapp.data.remote

import com.zeko.currencyconverterapp.data.models.CurrencyResponse
import com.zeko.currencyconverterapp.util.Constants.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrencyApi {

    @GET("v2.0/rates/latest")
    suspend fun getRates(
        @Query("apikey") key : String = API_KEY
    ) : Response<CurrencyResponse>
}