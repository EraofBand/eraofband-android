package com.example.eraofband.ui.main.board

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentBoardBinding

class BoardFragment : Fragment(){

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBoardBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            startActivity(Intent(activity, BoardPostActivity::class.java))
        }

        return binding.root
    }
}