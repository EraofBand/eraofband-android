package com.example.eraofband.ui.main.home

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentHomeBinding
import com.example.eraofband.ui.login.GlobalApplication
import com.example.eraofband.ui.main.home.notice.NoticeActivity
import com.google.android.material.tabs.TabLayoutMediator

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        sizeCheck()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.homeFab.setOnClickListener{
            val fabDialog = HomeFabDialog()
            fabDialog.show(fragmentManager!!, "homeFAB")
        }

        binding.homeNoticeIb.setOnClickListener {
            startActivity(Intent(context, NoticeActivity::class.java))
        }
        connectVP()
    }

    private fun connectVP() {
        val homeAdapter = HomeVPAdapter(this)
        binding.homeVp.adapter = homeAdapter

        TabLayoutMediator(binding.homeTb, binding.homeVp) { tab, position ->
            when (position) {
                0 -> tab.text = "세션 매칭"
                1 -> tab.text = "레슨 매칭"
                2 -> tab.text = "찜한 밴드"
                3 -> tab.text = "찜한 레슨"
            }
        }.attach()
    }

    private fun sizeCheck() {
        val display = activity!!.windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)  // 상단바 등을 제외한 스크린 전체 크기 구하기

        GlobalApplication.width = size.x
        GlobalApplication.height = size.y

        Log.d("SIZECHECK", "$size.x $size.y")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}