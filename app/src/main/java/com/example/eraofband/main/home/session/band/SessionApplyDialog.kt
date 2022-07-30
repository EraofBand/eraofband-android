package com.example.eraofband.main.home.session.band

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogSessionApplyBinding
import com.example.eraofband.login.GlobalApplication
import com.example.eraofband.remote.applyBand.ApplyBandResult
import com.example.eraofband.remote.applyBand.ApplyBandService
import com.example.eraofband.remote.applyBand.ApplyBandView
import com.example.eraofband.remote.applyLesson.ApplyLessonResult
import com.example.eraofband.remote.applyLesson.ApplyLessonView
import com.example.eraoflesson.remote.applyLesson.ApplyLessonService

class SessionApplyDialog(private val code: String, private val jwt: String, private val session: Int, private val bandIdx: Int): DialogFragment(), ApplyBandView, ApplyLessonView {

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

        if(code == "apply") {  // 세션 지원의 경우
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
        }
        else if(code == "lesson") {
            binding.sessionApplyTitleTv.text = "세션 지원"
            binding.sessionApplyContentTv.text = "레슨을 신청하시겠습니까?"

            binding.sessionApplyCancelTv.text = "취소하기"
            binding.sessionApplyAcceptTv.text = "신청하기"

            val applyLesson = ApplyLessonService()
            applyLesson.setApplyView(this)

            binding.sessionApplyAcceptTv.setOnClickListener {  // 지원하기 누르면 API 연동 후 다음 다이얼로그로 넘어감
                applyLesson.applyLesson(jwt, bandIdx)
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

    override fun onApplySuccess(result: ApplyBandResult) {
        // 지원 성공
        Log.d("APPLYBAND/SUC", result.toString())

        // 다음 dialog로 넘어감
        val completeDialog = SessionCompleteDialog(code)
        completeDialog.isCancelable = false
        completeDialog.show(activity!!.supportFragmentManager, "complete")

        dismiss()
    }

    override fun onApplyFailure(code: Int, message: String) {
        Log.d("APPLYBAND/FAIL", "$code $message")
    }

    override fun onApplyLessonSuccess(result: ApplyLessonResult) {
        Log.d("APPLY/SUCCESS", result.toString())

        // 다음 dialog로 넘어감
        val completeDialog = SessionCompleteDialog(code)
        completeDialog.isCancelable = false


        completeDialog.show(activity!!.supportFragmentManager, "complete")

        dismiss()
    }

    override fun onApplyLessonFailure(code: Int, message: String) {
        Log.d("APPLY/FAIL", "$code $message")

        // 중복 신청 불가 다이얼로그로 넘아감
        val completeDialog = SessionCompleteDialog("fail")
        completeDialog.isCancelable = false
        completeDialog.show(activity!!.supportFragmentManager, "fail")

        dismiss()
    }
}
