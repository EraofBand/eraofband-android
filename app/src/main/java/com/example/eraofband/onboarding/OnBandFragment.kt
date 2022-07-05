package com.example.eraofband.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentOnBandBinding


class OnBandFragment : Fragment() {
    lateinit var binding : FragmentOnBandBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnBandBinding.inflate(inflater,container,false)


        return binding.root
    }
}