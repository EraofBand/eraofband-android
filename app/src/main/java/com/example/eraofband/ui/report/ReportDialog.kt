package com.example.eraofband.ui.report

import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.example.eraofband.R
import com.example.eraofband.data.Report
import com.example.eraofband.databinding.DialogReportBinding
import com.example.eraofband.remote.report.ReportService
import com.example.eraofband.remote.report.ReportView
import com.example.eraofband.ui.login.GlobalApplication

class ReportDialog(private val jwt: String, private val location: Int, private val locationIdx: Int, private val userIdx: Int): DialogFragment(), ReportView {
    private lateinit var binding: DialogReportBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogReportBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        val reportService = ReportService()
        reportService.setReportView(this)

        binding.reportAcceptTv.setOnClickListener {
            val message = binding.reportEt.text.toString()
            reportService.report(jwt, Report(message, location, locationIdx, userIdx))
        }

        binding.reportCancelTv.setOnClickListener { dismiss() }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 다이얼로그 크기 지정
        dialog?.window?.setLayout((GlobalApplication.width - 40.toPx()), ((GlobalApplication.width - 40.toPx()) * 0.75).toInt())
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

//    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
//        // EditText를 제외한 영역을 누르면 키보드를 내려줌
//        val focusView = requireActivity().currentFocus
//        if (focusView != null && ev != null) {
//            val rect = Rect()
//            focusView.getGlobalVisibleRect(rect)
//            val x = ev.x.toInt()
//            val y = ev.y.toInt()
//
//            if (!rect.contains(x, y)) {
//                val inputMethodManager = getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
//                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
//                focusView.clearFocus()
//            }
//        }
//        return super.dispatchTouchEvent(ev)
//    }

    override fun onReportSuccess(result: String) {
        Log.d("REPORT/SUC", result)
        Toast.makeText(dialog?.context, getString(R.string.report_suc), Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onReportFailure(code: Int, message: String) {
        Log.d("REPORT/FAIL", "$code $message")
    }

}