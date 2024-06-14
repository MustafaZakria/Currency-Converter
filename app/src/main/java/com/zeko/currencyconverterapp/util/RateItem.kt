package com.zeko.currencyconverterapp.util

class RateItem(
    private val country: String,
    private val rate: Double
) {
    override fun toString(): String {
        return "$rate $country"
    }

    fun getCounty() : String {
        return country
    }
}
