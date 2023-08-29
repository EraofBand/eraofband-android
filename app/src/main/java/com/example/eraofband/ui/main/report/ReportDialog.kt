package com.example.eraofband.ui.main.report

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.eraofband.R
import com.example.eraofband.data.Report
import com.example.eraofband.databinding.DialogReportBinding
import com.example.eraofband.remote.report.ReportService
import com.example.eraofband.remote.report.ReportView
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtService
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtView
import com.example.eraofband.remote.user.refreshjwt.RefreshResult
import com.example.eraofband.ui.login.GlobalApplication
import com.example.eraofband.ui.setOnSingleClickListener

class ReportDialog(private val jwt: String, private val location: Int, private val locationIdx: Int,
                   private val userIdx: Int): DialogFragment(), ReportView, RefreshJwtView {

    private lateinit var binding: DialogReportBinding
    private val reportService = ReportService()
    private val refreshJwtService = RefreshJwtService();

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogReportBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        reportService.setReportView(this)
        refreshJwtService.setRefreshView(this)

        binding.reportAcceptTv.setOnSingleClickListener {
            val message = "${binding.reportEt.text.trim()}"

            if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
                refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, userIdx)
            } else {
                reportService.report(jwt, Report(message, location, locationIdx, userIdx))
            }
        }

        binding.reportCancelTv.setOnClickListener { dismiss() }

        binding.root.setOnClickListener { hideKeyboard() }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        // 다이얼로그 크기 지정
        dialog?.window?.setLayout((GlobalApplication.width - 40.toPx()), ((GlobalApplication.width - 40.toPx()) * 0.75).toInt())
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = dialog?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(dialog?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun getUser(): SharedPreferences {
        return requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onReportSuccess(result: String) {
        Log.d("REPORT/SUC", result)
        Toast.makeText(dialog?.context, getString(R.string.report_suc), Toast.LENGTH_SHORT).show()
        dismiss()
    }

    override fun onReportFailure(code: Int, message: String) {
        Log.d("REPORT/FAIL", "$code $message")
    }

    override fun onPatchSuccess(code: Int, result: RefreshResult) {
        Log.d("REFRESH/SUC", "$result")

        val message = "${binding.reportEt.text.trim()}"
        reportService.report(jwt, Report(message, location, locationIdx, userIdx))
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("REFRESH/FAIL", "$code $message")
    }

}