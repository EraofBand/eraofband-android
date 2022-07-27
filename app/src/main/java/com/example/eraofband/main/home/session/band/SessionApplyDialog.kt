package com.example.eraofband.main.home.session.band

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogSessionApplyBinding
import com.example.eraofband.login.GlobalApplication

class SessionApplyDialog(private val code: String): DialogFragment() {

    private lateinit var binding: DialogSessionApplyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionApplyBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)


        if(code == "apply") {  // 세션 지원의 경우
            binding.sessionApplyTitleTv.text = "세션 지원"
            binding.sessionApplyContentTv.text = "밴드에 지원하시겠습니까?"

            binding.sessionApplyCancelTv.text = "취소하기"
            binding.sessionApplyAcceptTv.text = "지원하기"

            binding.sessionApplyAcceptTv.setOnClickListener {  // 지원하기 누르면 다음 다이얼로그로 넘어감
                val completeDialog = SessionCompleteDialog(code)
                completeDialog.isCancelable = false
                completeDialog.show(activity!!.supportFragmentManager, "complete")

                dismiss()
            }

            binding.sessionApplyCancelTv.setOnClickListener { dismiss() }  // 취소하기를 누르면 다이얼로그 종료
        }
        else {  // 세션 선발의 경우
            binding.sessionApplyTitleTv.text = "세션 모집"
            binding.sessionApplyContentTv.text = "OOO님을 선발하시겠습니까?"

            binding.sessionApplyCancelTv.text = "거절하기"
            binding.sessionApplyAcceptTv.text = "수락하기"

            binding.sessionApplyAcceptTv.setOnClickListener {  // 수락하기를 누르면 다음 다이얼로그로 넘어감
                val completeDialog = SessionCompleteDialog("accept")
                completeDialog.isCancelable = false
                completeDialog.show(activity!!.supportFragmentManager, "complete")

                dismiss()
            }

            binding.sessionApplyCancelTv.setOnClickListener {  // 거절하기를 누르면 다음 다이얼로그로 넘어감
                val completeDialog = SessionCompleteDialog("cancel")
                completeDialog.isCancelable = false
                completeDialog.show(activity!!.supportFragmentManager, "complete")

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
}