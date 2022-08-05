package com.example.eraofband.ui.main.home.lesson

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogSessionApplyBinding
import com.example.eraofband.remote.lesson.applyLesson.ApplyLessonResult
import com.example.eraofband.remote.lesson.applyLesson.ApplyLessonView
import com.example.eraofband.ui.login.GlobalApplication
import com.example.eraofband.ui.main.home.session.band.SessionCompleteDialog
import com.example.eraoflesson.remote.applyLesson.ApplyLessonService

class LessonApplyDialog(private val teacherIdx: Int, private val jwt: String, private val userIdx: Int, private val lessonIdx: Int): DialogFragment(), ApplyLessonView {

    private lateinit var binding: DialogSessionApplyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionApplyBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)


        binding.sessionApplyTitleTv.text = "세션 지원"
        binding.sessionApplyContentTv.text = "레슨을 신청하시겠습니까?"

        binding.sessionApplyCancelTv.text = "취소하기"
        binding.sessionApplyAcceptTv.text = "신청하기"

        val applyLesson = ApplyLessonService()
        applyLesson.setApplyView(this)

        binding.sessionApplyAcceptTv.setOnClickListener {  // 지원하기 누르면 API 연동 후 다음 다이얼로그로 넘어감
            if (userIdx == teacherIdx) {
                Toast.makeText(context, "자신이 개설한 레슨에는 신청할 수 없습니다.", Toast.LENGTH_SHORT).show()
                dismiss()
            } else {
                applyLesson.applyLesson(jwt, lessonIdx)
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

    override fun onApplyLessonSuccess(result: ApplyLessonResult) {
        Log.d("APPLY/SUCCESS", result.toString())

        val completeDialog = SessionCompleteDialog()
        completeDialog.isCancelable = false
        completeDialog.show(activity!!.supportFragmentManager, "lessonApply")
        dismiss()

    }

    override fun onApplyLessonFailure(code: Int, message: String) {
        Log.d("APPLY/FAIL", "$code $message")

        // 중복 신청 불가 다이얼로그로 넘아감
        val completeDialog = SessionCompleteDialog()
        completeDialog.isCancelable = false
        completeDialog.show(activity!!.supportFragmentManager, "duplicateLesson")

        dismiss()
    }
}
