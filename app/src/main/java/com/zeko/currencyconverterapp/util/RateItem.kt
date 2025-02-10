package com.zeko.currencyconverterapp.util

data class RateItem(
    private val currency: String,
    private val rate: Double,
    private val isFavourite: Boolean = false
) {
    override fun toString() = "$rate $currency"

    fun getCurrency() = currency

    fun getFavouriteStatus() = isFavourite

    fun getRate() = rate

}
