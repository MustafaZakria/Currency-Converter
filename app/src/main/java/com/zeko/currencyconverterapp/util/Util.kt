package com.zeko.currencyconverterapp.util

import com.zeko.currencyconverterapp.data.models.Rates
import com.zeko.currencyconverterapp.sharedPref.CurrencySharedPreference
import kotlin.math.round

object Util {

    fun getRateForCurrency(currency: String, rates: Rates): String? {
        return when (currency) {
            "CAD" -> rates.CAD
            "HKD" -> rates.HKD
            "ISK" -> rates.ISK
            "EUR" -> rates.EUR
            "PHP" -> rates.PHP
            "DKK" -> rates.DKK
            "HUF" -> rates.HUF
            "CZK" -> rates.CZK
            "AUD" -> rates.AUD
            "RON" -> rates.RON
            "SEK" -> rates.SEK
            "IDR" -> rates.IDR
            "INR" -> rates.INR
            "BRL" -> rates.BRL
            "RUB" -> rates.RUB
            "HRK" -> rates.HRK
            "JPY" -> rates.JPY
            "THB" -> rates.THB
            "CHF" -> rates.CHF
            "SGD" -> rates.SGD
            "PLN" -> rates.PLN
            "BGN" -> rates.BGN
            "CNY" -> rates.CNY
            "NOK" -> rates.NOK
            "NZD" -> rates.NZD
            "ZAR" -> rates.ZAR
            "USD" -> rates.USD
            "MXN" -> rates.MXN
            "ILS" -> rates.ILS
            "GBP" -> rates.GBP
            "EGP" -> rates.EGP
            else -> null
        }
    }

    fun calcCurrencyRate(
        currency: String,
        rates: Rates,
        mainBase: String,
        responseBase: String = Constants.BASE
    ): Double {
        var rate = getRateForCurrency(currency, rates)?.toDouble() ?: 0.0
        if (mainBase != responseBase) {
            val baseValue = getRateForCurrency(mainBase, rates)?.toDouble() ?: 1.0
            rate /= baseValue
        }
        rate = round(rate * 100) / 100

        return rate
    }

    fun isCurrencyFavourite(currency: String, sharedPref: CurrencySharedPreference): Boolean {
        return sharedPref.getFavCurrencies()?.contains(currency) ?: false
    }
}