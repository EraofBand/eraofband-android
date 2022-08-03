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
import com.example.eraofband.remote.band.applyBand.ApplyBandResult
import com.example.eraofband.remote.band.applyBand.ApplyBandService
import com.example.eraofband.remote.band.applyBand.ApplyBandView

class SessionApplyDialog(private val jwt: String, private val session: Int, private val bandIdx: Int, private val position: Int): DialogFragment(),
    ApplyBandView {

    private lateinit var binding: DialogSessionApplyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionApplyBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        Log.d("CHECKING", "$jwt $session $bandIdx")

        binding.sessionApplyTitleTv.text = "세션 지원"
        binding.sessionApplyContentTv.text = "밴드에 지원하시겠습니까?"

        binding.sessionApplyCancelTv.text = "취소하기"
        binding.sessionApplyAcceptTv.text = "지원하기"

        val applyService = ApplyBandService()
        applyService.setApplyView(this)

        binding.sessionApplyAcceptTv.setOnClickListener {  // 지원하기 누르면 API 연동 후 다음 다이얼로그로 넘어감
            applyService.applyBand(jwt, bandIdx, session)
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

    override fun onApplySuccess(result: ApplyBandResult) {
        // 지원 성공
        Log.d("APPLYBAND/SUC", result.toString())

        // 다음 dialog로 넘어감
        val completeDialog = SessionCompleteDialog("apply")
        completeDialog.isCancelable = false
        completeDialog.show(activity!!.supportFragmentManager, "complete")

        dismiss()
    }

    override fun onDuplicateApply(code: Int, message: String) {
        Log.d("APPLYBAND/FAIL", "$code $message")

        // 다음 dialog로 넘어감
        val completeDialog = SessionCompleteDialog("duplicate")
        completeDialog.isCancelable = false
        completeDialog.show(activity!!.supportFragmentManager, "complete")

        dismiss()
    }

    override fun onApplyFailure(code: Int, message: String) {
        Log.d("APPLYBAND/FAIL", "$code $message")
    }
}