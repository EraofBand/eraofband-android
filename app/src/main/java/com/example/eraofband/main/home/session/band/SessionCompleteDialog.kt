package com.example.eraofband.main.home.session.band

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogSessionCompleteBinding
import com.example.eraofband.login.GlobalApplication

class SessionCompleteDialog: DialogFragment() {

    private lateinit var binding: DialogSessionCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionCompleteBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        binding.sessionCompleteAcceptTv.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 다이얼로그 크기 지정
        dialog?.window?.setLayout((GlobalApplication.width - 40.toPx()), ((GlobalApplication.width - 40.toPx()) * 0.75).toInt())
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}