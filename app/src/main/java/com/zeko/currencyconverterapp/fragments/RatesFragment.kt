package com.zeko.currencyconverterapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.databinding.FragmentRatesBinding

class RatesFragment : Fragment() {
    lateinit var binding: FragmentRatesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_rates, container, false)

        return binding.root
    }

}