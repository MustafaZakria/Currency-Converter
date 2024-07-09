package com.zeko.currencyconverterapp.sharedPref

import android.content.SharedPreferences
import android.util.Log
import com.zeko.currencyconverterapp.util.Constants.FAV_CURRENCY_KEY

class CurrencySharedPreference(private val sharedPreferences: SharedPreferences) {
    fun putFavCurrency(currency: String) {
        val favCurrencies = sharedPreferences.getString(FAV_CURRENCY_KEY, "") + currency + " "
        sharedPreferences.edit().putString(FAV_CURRENCY_KEY, favCurrencies).apply()
    }

    fun getFavCurrencies(): List<String>? {
        val favCurrencies = sharedPreferences.getString(FAV_CURRENCY_KEY, "")
        return favCurrencies?.split(" ")
    }

    fun removeFavCurrency(currency: String) {
        val list = sharedPreferences.getString(FAV_CURRENCY_KEY, "")?.split(" ")
        var result = ""
        if(list?.contains(currency) == true) {
            list.forEach { item ->
                if(item != currency) {
                    result = "$result$item "
                }
            }
        }
        sharedPreferences.edit().putString(FAV_CURRENCY_KEY, result).apply()
    }
}