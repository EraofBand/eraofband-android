package com.example.eraofband.main.home.session.band

import android.content.Context.MODE_PRIVATE
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

class SessionCompleteDialog(private val code: String): DialogFragment() {

    private lateinit var binding: DialogSessionCompleteBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionCompleteBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        // 다이얼로그를 닫았을 때 해당 아이템이 사라지게 만들기 위해서

        if(code == "apply") {  // 지원하기
            binding.sessionCompleteTitleTv.text = "지원 완료"
            binding.sessionCompleteContentTv.text = "좋은 소식을 기다리겠습니다 :)"

            binding.sessionCompleteAcceptTv.text = "완료"

            binding.sessionCompleteAcceptTv.setOnClickListener { dismiss() }
        }
        else if(code == "accept") {  // 지원 수락
            binding.sessionCompleteTitleTv.text = "수락 완료"
            binding.sessionCompleteContentTv.text = "이제부터 같은 밴드에 소속됩니다!"

            binding.sessionCompleteAcceptTv.text = "완료"

            binding.sessionCompleteAcceptTv.setOnClickListener {
                applyListener.applyAccept()
                dismiss()
            }

        }
        else {  // 지원 거절
            binding.sessionCompleteTitleTv.text = "거절 완료"
            binding.sessionCompleteContentTv.text = "거절이 완료되었습니다."

            binding.sessionCompleteAcceptTv.text = "완료"

            binding.sessionCompleteAcceptTv.setOnClickListener {
                applyListener.applyReject()
                dismiss()
            }

        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 다이얼로그 크기 지정
        dialog?.window?.setLayout((GlobalApplication.width - 40.toPx()), ((GlobalApplication.width - 40.toPx()) * 0.75).toInt())
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    interface ApplyDecision {
        fun applyAccept()
        fun applyReject()
    }

    private lateinit var applyListener: ApplyDecision
    fun setDialogListener(apply: ApplyDecision) {
        applyListener = apply
    }

}