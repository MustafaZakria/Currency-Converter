package com.zeko.currencyconverterapp.util

class RateItem(
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

    fun copy(
        currency: String = this.currency, rate: Double = this.rate,
        isFavourite: Boolean = this.isFavourite
    ): RateItem {
        return RateItem(currency, rate, isFavourite)
    }
}
