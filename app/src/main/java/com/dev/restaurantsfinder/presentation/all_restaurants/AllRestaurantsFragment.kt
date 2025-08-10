package com.dev.restaurantsfinder.presentation.all_restaurants

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dev.restaurantsfinder.databinding.AllRestaurentsFragmentBinding
import com.dev.restaurantsfinder.base.BaseFragment


class AllRestaurantsFragment : BaseFragment() {

    private var _binding: AllRestaurentsFragmentBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = AllRestaurentsFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


}