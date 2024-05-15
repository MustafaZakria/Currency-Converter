package com.zeko.currencyconverterapp.data.models

data class CurrencyResponse(
    val base: String,
    val date: String,
    val rates: Rates
)