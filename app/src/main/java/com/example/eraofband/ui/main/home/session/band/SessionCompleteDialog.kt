package com.example.eraofband.ui.main.home.session.band

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
import com.example.eraofband.ui.login.GlobalApplication

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

        // 다이얼로그를 닫았을 때 해당 아이템이 사라지게 만들기 위해서

        when(tag) {
            "sessionApply" -> {  // 지원하기
                binding.sessionCompleteTitleTv.text = "지원 완료"
                binding.sessionCompleteContentTv.text = "좋은 소식을 기다리겠습니다 :)"

                binding.sessionCompleteAcceptTv.text = "완료"

                binding.sessionCompleteAcceptTv.setOnClickListener { dismiss() }
            }
            "duplicateBand" -> {  // 중복 지원
                binding.sessionCompleteTitleTv.text = "중복 신청 불가"
                binding.sessionCompleteContentTv.text = "이미 밴드에 지원했습니다!"

                binding.sessionCompleteAcceptTv.text = "완료"

                binding.sessionCompleteAcceptTv.setOnClickListener { dismiss() }
            }
            "acceptApply" -> {  // 지원 수락
                binding.sessionCompleteTitleTv.text = "수락 완료"
                binding.sessionCompleteContentTv.text = "이제부터 같은 밴드에 소속됩니다!"

                binding.sessionCompleteAcceptTv.text = "완료"

                binding.sessionCompleteAcceptTv.setOnClickListener {
                    applyListener.applyAccept()
                    dismiss()
                }
            }
            "rejectApply" -> {  // 지원 거절
                binding.sessionCompleteTitleTv.text = "거절 완료"
                binding.sessionCompleteContentTv.text = "거절이 완료되었습니다."

                binding.sessionCompleteAcceptTv.text = "완료"

                binding.sessionCompleteAcceptTv.setOnClickListener {
                    applyListener.applyReject()
                    dismiss()
                }
            }
            "lessonApply" -> {  // 레슨 지원
                binding.sessionCompleteTitleTv.text = "신청 완료"
                binding.sessionCompleteContentTv.text = "강사님과 함께 열심히 배워보아요!"

                binding.sessionCompleteAcceptTv.text = "완료"

                binding.sessionCompleteAcceptTv.setOnClickListener { dismiss() }
            }
            "duplicateLesson" -> {  // 중복 지원
                binding.sessionCompleteTitleTv.text = "중복 신청 불가"
                binding.sessionCompleteContentTv.text = "이미 레슨을 수강하고 있습니다!"

                binding.sessionCompleteAcceptTv.text = "완료"

                binding.sessionCompleteAcceptTv.setOnClickListener { dismiss() }
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