package com.example.eraofband.ui.main.board

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentBoardBinding

class BoardFragment : Fragment() {

    private var _binding: FragmentBoardBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var feedRVAdapter: BoardFeedRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBoardBinding.inflate(inflater, container, false)


        return binding.root
    }

    override fun onResume() {
        super.onResume()

        initFeedRV()
    }

    private fun initFeedRV() {
        feedRVAdapter = BoardFeedRVAdapter(getJwt()!!, requireContext())
        binding.boardFeedRv.adapter = feedRVAdapter
        binding.boardFeedRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }
}