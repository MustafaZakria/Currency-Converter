package com.zeko.currencyconverterapp.ui.fragments

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.databinding.FragmentConverterBinding
import com.zeko.currencyconverterapp.main.MainViewModel
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.EntryPoint

@EntryPoint
class ConverterFragment : Fragment() {

    lateinit var binding : FragmentConverterBinding
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_converter, container, false)

        binding.lifecycleOwner = this

        binding.btnConvert.setOnClickListener{
            val currencyFrom = binding.spFromCurrency.selectedItem.toString()
            val currencyTo = binding.spToCurrency.selectedItem.toString()
            val amount = binding.etAmount.toString()

            viewModel.convert(currencyFrom, currencyTo, amount)
        }

        viewModel.convertedValue.observe(viewLifecycleOwner, Observer { event ->
            when(event) {
                is MainViewModel.CurrencyEvent.Success -> {
                    binding.progressBar.isVisible = false
                    binding.tvResult.text = event.resultText
                    binding.tvResult.setTextColor(Color.BLACK)
                }
                is MainViewModel.CurrencyEvent.Failure -> {
                    binding.progressBar.isVisible = false
                    binding.tvResult.text = event.errorText
                    binding.tvResult.setTextColor(Color.RED)
                }
                is MainViewModel.CurrencyEvent.Loading -> {
                    binding.progressBar.isVisible = true
                }
                else -> Unit
            }
        })
        return binding.root
    }

}