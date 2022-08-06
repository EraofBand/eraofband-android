package com.example.eraofband.ui.main.home.session.band

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogSessionApplyBinding
import com.example.eraofband.remote.band.deleteBand.DeleteBandService
import com.example.eraofband.remote.band.deleteBand.DeleteBandView
import com.example.eraofband.remote.band.deleteUserBand.DeleteUserBandService
import com.example.eraofband.remote.band.deleteUserBand.DeleteUserBandView
import com.example.eraofband.remote.lesson.deleteLesson.DeleteLessonService
import com.example.eraofband.remote.lesson.deleteLesson.DeleteLessonView
import com.example.eraofband.remote.lesson.deleteUserLesson.DeleteUserLessonService
import com.example.eraofband.remote.lesson.deleteUserLesson.DeleteUserLessonView
import com.example.eraofband.remote.portfolio.deletePofol.DeletePofolResponse
import com.example.eraofband.remote.portfolio.deletePofol.DeletePofolService
import com.example.eraofband.remote.portfolio.deletePofol.DeletePofolView
import com.example.eraofband.ui.login.GlobalApplication

class BandDeleteDialog(private val jwt: String, private val userIdx: Int, private val idx: Int)
    : DeletePofolView, DialogFragment(), DeleteBandView, DeleteUserBandView, DeleteLessonView, DeleteUserLessonView {

    private lateinit var binding: DialogSessionApplyBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogSessionApplyBinding.inflate(inflater, container, false)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)

        when(tag) {
            "deletePortfolio" -> {
                binding.sessionApplyTitleTv.text = "포트폴리오 삭제"
                binding.sessionApplyContentTv.text = "포트폴리오를 삭제하시겠습니까?"
                binding.sessionApplyAcceptTv.text = "삭제하기"

                binding.sessionApplyAcceptTv.setOnClickListener {
                    // 포폴 삭제
                    val deletePofolService = DeletePofolService()
                    deletePofolService.setDeleteView(this)
                    deletePofolService.deletePortfolio(jwt, idx, userIdx)
                }

                binding.sessionApplyCancelTv.setOnClickListener { dismiss() }
            }
            "deleteBand" -> {
                binding.sessionApplyTitleTv.text = "밴드 삭제"
                binding.sessionApplyContentTv.text = "밴드를 삭제하시겠습니까?"
                binding.sessionApplyAcceptTv.text = "삭제하기"

                binding.sessionApplyAcceptTv.setOnClickListener {
                    // 밴드 삭제
                    val deleteBandService = DeleteBandService()
                    deleteBandService.setDeleteView(this)
                    deleteBandService.deleteBand(jwt, idx, userIdx)
                    Handler(Looper.getMainLooper()).postDelayed({
                        requireActivity().finish()
                    }, 100)
                }

                binding.sessionApplyCancelTv.setOnClickListener { dismiss() }
            }
            "resignBand" -> {
                binding.sessionApplyTitleTv.text = "밴드 탈퇴"
                binding.sessionApplyContentTv.text = "밴드를 탈퇴하시겠습니까?"
                binding.sessionApplyAcceptTv.text = "탈퇴하기"

                binding.sessionApplyAcceptTv.setOnClickListener {
                    // 밴드 탈퇴
                    val deleteUserBandService = DeleteUserBandService()
                    deleteUserBandService.setDeleteView(this)
                    deleteUserBandService.deleteUserBand(jwt, idx)
                    Handler(Looper.getMainLooper()).postDelayed({
                        requireActivity().finish()
                    }, 100)
                }

                binding.sessionApplyCancelTv.setOnClickListener { dismiss() }
            }
            "deleteLesson" -> {
                binding.sessionApplyTitleTv.text = "레슨 삭제"
                binding.sessionApplyContentTv.text = "레슨을 삭제하시겠습니까?"
                binding.sessionApplyAcceptTv.text = "삭제하기"

                binding.sessionApplyAcceptTv.setOnClickListener {
                    //레슨 삭제
                    val deleteLessonService = DeleteLessonService()
                    deleteLessonService.setDeleteView(this)
                    deleteLessonService.deleteLesson(jwt, idx, userIdx)
                    Handler(Looper.getMainLooper()).postDelayed({
                        requireActivity().finish()
                    }, 100)
                }

                binding.sessionApplyCancelTv.setOnClickListener { dismiss() }
            }
            "resignLesson" -> {
                binding.sessionApplyTitleTv.text = "레슨 탈퇴"
                binding.sessionApplyContentTv.text = "레슨을 탈퇴하시겠습니까?"
                binding.sessionApplyAcceptTv.text = "탈퇴하기"

                binding.sessionApplyAcceptTv.setOnClickListener {
                    // 레슨 탈퇴
                    val deleteUserLessonService = DeleteUserLessonService()
                    deleteUserLessonService.setDeleteView(this)
                    deleteUserLessonService.deleteUserLesson(jwt, idx)
                    Handler(Looper.getMainLooper()).postDelayed({
                        requireActivity().finish()
                    }, 100)
                }

                binding.sessionApplyCancelTv.setOnClickListener { dismiss() }
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

    override fun onDeleteSuccess(code: Int, result: String) {  // 포트폴리오 삭제
        Log.d("DELETE/SUC", result)

        deleteListener.deletePortfolio()
        dismiss()
    }

    override fun onDeleteFailure(response: DeletePofolResponse) {
        Log.d("DELETE/FAIL", response.toString())
    }

    override fun onDeleteSuccess(result: String) {
        Log.d("DELETE / SUCCESS", result)

        dismiss()
    }

    override fun onDeleteFailure(code: Int, message: String) {
        Log.d("DELETE / FAIL", "$code $message")
    }

    override fun onDeleteUserSuccess(result: String) {
        Log.d("DELETEUSER / SUCCESS", result)

        dismiss()
    }

    override fun onDeleteUserFailure(code: Int, message: String) {
        Log.d("DELETEUSER / FAIL", "$code $message")
    }

    interface DeleteListener {
        fun deletePortfolio()
    }

    private lateinit var deleteListener: DeleteListener
    fun setDialogListener(deleteListener: DeleteListener) {
        this.deleteListener = deleteListener
    }
}