package com.zeko.currencyconverterapp.util

//Sealed class represent restricted hierarchies in situations where you want to ensure that a type can only have a specific set of subclasses.
sealed class Resource<T>(val data: T?, val message: String?) {
    class Success<T>(data: T) : Resource<T>(data, null)
    class Error<T>(message: String) : Resource<T>(null, message)
}