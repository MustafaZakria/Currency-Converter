package com.zeko.currencyconverterapp.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.zeko.currencyconverterapp.R
import com.zeko.currencyconverterapp.adapters.FavRateClickListener
import com.zeko.currencyconverterapp.adapters.RateAdapter
import com.zeko.currencyconverterapp.databinding.FragmentRatesBinding
import com.zeko.currencyconverterapp.ui.viewmodels.RatesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RatesFragment : Fragment() {
    private lateinit var binding: FragmentRatesBinding
    private val viewModel: RatesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_rates, container, false)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        setUpRecycleView()
        return binding.root
    }

    private fun setUpRecycleView() {
        val adapter = RateAdapter(FavRateClickListener { currency ->
            viewModel.addFavRate(currency)
        })
        val rvRater = binding.rvRates
        rvRater.adapter = adapter
        rvRater.layoutManager = LinearLayoutManager(requireContext())

        viewModel.rateItems.observe(viewLifecycleOwner, Observer { rateItems ->
            if (rateItems.isEmpty()) {
                binding.progressBar.isVisible = true
            } else {
                binding.progressBar.isVisible = false
                val spValue = viewModel.spinnerValue.value!!
                val list = rateItems.filter { item -> item.getCurrency() != spValue }
                adapter.submitList(list)
            }
        })
    }


}