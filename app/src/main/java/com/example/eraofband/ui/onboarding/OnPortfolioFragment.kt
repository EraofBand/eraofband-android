package com.example.eraofband.ui.onboarding

import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.eraofband.databinding.FragmentOnPortfolioBinding


class OnPortfolioFragment : Fragment() {
    lateinit var binding : FragmentOnPortfolioBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentOnPortfolioBinding.inflate(inflater,container,false)

        setColor()

        return binding.root
    }

    private fun setColor() {  // 글씨 파란색, 두껍게 만들기
        val string = binding.onportfolioPortfolioTv.text  // 텍스트 가져옴
        val spannableString = SpannableString(string)  //객체 생성

        val word = "나만의 음악 포트폴리오"
        val start = string.indexOf(word)
        val end = start + word.length

        spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#1864FD")), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(StyleSpan(Typeface.BOLD), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        binding.onportfolioPortfolioTv.text = spannableString
    }
}