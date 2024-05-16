package com.zeko.currencyconverterapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.databinding.FragmentConverterBinding

class ConverterFragment : Fragment() {

    lateinit var binding : FragmentConverterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_converter, container, false)

        return binding.root
    }
}