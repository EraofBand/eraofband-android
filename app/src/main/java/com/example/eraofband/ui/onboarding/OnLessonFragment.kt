package com.example.eraofband.ui.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentOnLessonBinding


class OnLessonFragment : Fragment() {
    lateinit var binding : FragmentOnLessonBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnLessonBinding.inflate(inflater,container,false)


        return binding.root
    }
}