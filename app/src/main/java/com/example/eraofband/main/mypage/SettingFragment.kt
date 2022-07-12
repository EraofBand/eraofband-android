package com.example.eraofband.main.mypage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentSettingBinding
import com.example.eraofband.main.MainActivity


class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingBinding.inflate(inflater,container,false)

        binding.settingBackIb.setOnClickListener{
            (context as MainActivity).onBackPressed()
        }

        return binding.root
    }
}