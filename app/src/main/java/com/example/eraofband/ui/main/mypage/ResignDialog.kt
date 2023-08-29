package com.example.eraofband.ui.main.mypage

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogSessionApplyBinding
import com.example.eraofband.ui.login.GlobalApplication
import com.example.eraofband.ui.setOnSingleClickListener

class ResignDialog(private val code: Int): DialogFragment() {

    private lateinit var binding: DialogSessionApplyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionApplyBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        if(code == 0) {  // 로그아웃
            binding.sessionApplyTitleTv.text = "로그아웃"
            binding.sessionApplyContentTv.text = "로그아웃하시겠습니까?"

            binding.sessionApplyCancelTv.text = "취소"
            binding.sessionApplyAcceptTv.text = "확인"

            binding.sessionApplyAcceptTv.setOnClickListener {  // 탈퇴하기
                resignListener.logout()
            }
        }
        else {  // 회원 탈퇴
            binding.sessionApplyTitleTv.text = "회원 탈퇴"
            binding.sessionApplyContentTv.text = "한 번만 다시 생각해주세요ㅠㅠ"

            binding.sessionApplyCancelTv.text = "취소하기"
            binding.sessionApplyAcceptTv.text = "탈퇴하기"

            binding.sessionApplyAcceptTv.setOnSingleClickListener {  // 탈퇴하기
                resignListener.resign()
            }
        }

        binding.sessionApplyCancelTv.setOnClickListener { dismiss() }  // 취소하기를 누르면 다이얼로그 종료

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 다이얼로그 크기 지정
        dialog?.window?.setLayout((GlobalApplication.width - 40.toPx()), ((GlobalApplication.width - 40.toPx()) * 0.75).toInt())
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    interface ResignListener {
        fun resign()
        fun logout()
    }

    private lateinit var resignListener: ResignListener
    fun setDialogListener(resignListener: ResignListener) {
        this.resignListener = resignListener
    }
}