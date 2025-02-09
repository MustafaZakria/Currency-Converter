package com.zeko.currencyconverterapp.util

data class RateItem(
    private val currency: String,
    private val rate: Double,
    var isFavourite: Boolean = false
) {
    override fun toString(): String {
        return "$rate $currency"
    }

    fun getCurrency(): String {
        return currency
    }

    fun getRate(): Double {
        return rate
    }

}
