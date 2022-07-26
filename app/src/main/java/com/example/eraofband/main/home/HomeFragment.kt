package com.example.eraofband.main.home

import android.content.Intent
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentHomeBinding
import com.example.eraofband.login.GlobalApplication
import com.example.eraofband.main.home.lesson.LessonMakeActivity
import com.example.eraofband.main.home.session.band.BandMakeActivity
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
        connectVP()
    }

    private fun fabPopupMenu() {
        val popupMenu = PopupMenu(context, binding.homeFab)
        popupMenu.menuInflater.inflate(R.menu.home_fab_menu, popupMenu.menu)
        popupMenu.show()

        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.menu_make_band -> {
                    startActivity(Intent(context, BandMakeActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
                R.id.menu_make_lesson -> {
                    startActivity(Intent(context, LessonMakeActivity::class.java))
                    return@setOnMenuItemClickListener true
                }
                else -> return@setOnMenuItemClickListener false
            }
        }
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