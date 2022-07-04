package com.example.eraofband

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    private lateinit var binding : FragmentBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentBoardBinding.inflate(inflater, container, false)

        return binding.root
    }
}