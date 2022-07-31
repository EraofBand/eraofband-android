package com.example.eraofband.ui.main.home.session.band

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogSessionApplyBinding
import com.example.eraofband.ui.login.GlobalApplication
import com.example.eraofband.remote.band.applyDecision.ApplyDecisionService
import com.example.eraofband.remote.band.applyDecision.ApplyDecisionView
import com.example.eraofband.remote.band.getBand.Applicants

class SessionDecisionDialog(private val bandIdx: Int, private val applicant: Applicants): DialogFragment(),
    ApplyDecisionView {

    private lateinit var binding: DialogSessionApplyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionApplyBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        // 세션 선발
        binding.sessionApplyTitleTv.text = "세션 모집"
        binding.sessionApplyContentTv.text = "${applicant.nickName}님을 선발하시겠습니까?"

        binding.sessionApplyCancelTv.text = "거절하기"
        binding.sessionApplyAcceptTv.text = "수락하기"

        val decisionService = ApplyDecisionService()
        decisionService.setDecisionView(this)

        binding.sessionApplyAcceptTv.setOnClickListener {  // 수락하기를 누르면 다음 다이얼로그로 넘어감
            decisionService.acceptApply(bandIdx, applicant.userIdx)
        }

        binding.sessionApplyCancelTv.setOnClickListener {  // 거절하기를 누르면 다음 다이얼로그로 넘어감
            decisionService.rejectApply(bandIdx, applicant.userIdx)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 다이얼로그 크기 지정
        dialog?.window?.setLayout((GlobalApplication.width - 40.toPx()), ((GlobalApplication.width - 40.toPx()) * 0.75).toInt())
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    override fun onAcceptSuccess(result: String) {
        Log.d("ACCEPT/SUC", result)
        applyListener.applyAccept(applicant.buSession)
    }

    override fun onAcceptFailure(code: Int, message: String) {
        Log.d("ACCEPT/FAIL", "$code $message")
    }

    override fun onRejectSuccess(result: String) {
        Log.d("REJECT/SUC", result)
        applyListener.applyReject(applicant.buSession)
    }

    override fun onRejectFailure(code: Int, message: String) {
        Log.d("REJECT/FAIL", "$code $message")
    }

    interface ApplyDecision {
        fun applyAccept(session: Int)
        fun applyReject(session: Int)
    }

    private lateinit var applyListener: ApplyDecision
    fun setDialogListener(apply: ApplyDecision) {
        applyListener = apply
    }
}