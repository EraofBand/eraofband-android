package com.example.eraofband.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentOnTogetherBinding


class OnTogetherFragment : Fragment() {
    lateinit var binding : FragmentOnTogetherBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnTogetherBinding.inflate(inflater,container,false)


        return binding.root
    }
}