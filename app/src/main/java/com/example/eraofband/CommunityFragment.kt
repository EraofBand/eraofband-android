package com.example.eraofband

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentCommunityBinding

class CommunityFragment : Fragment() {

    private lateinit var binding : FragmentCommunityBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentCommunityBinding.inflate(inflater, container, false)

        return binding.root
    }
}