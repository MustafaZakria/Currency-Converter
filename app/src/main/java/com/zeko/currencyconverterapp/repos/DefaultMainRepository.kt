package com.zeko.currencyconverterapp.repos

import com.zeko.currencyconverterapp.data.remote.CurrencyApi
import com.zeko.currencyconverterapp.data.models.CurrencyResponse
import com.zeko.currencyconverterapp.util.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultMainRepository @Inject constructor(
    private val api: CurrencyApi
): MainRepository {

    override suspend fun getRates(): Resource<CurrencyResponse> {
        return try {
            val response = api.getRates()
            val result = response.body()
            if(response.isSuccessful && result != null) {
                Resource.Success(result)
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "An error occurred")
        }
    }
}