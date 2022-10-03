package com.example.eraofband.ui.main.home.notice

import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityNoticeBinding
import com.example.eraofband.remote.notice.deleteNotice.DeleteNoticeService
import com.example.eraofband.remote.notice.deleteNotice.DeleteNoticeView
import com.example.eraofband.remote.notice.getNotice.GetNoticeResult
import com.example.eraofband.remote.notice.getNotice.GetNoticeService
import com.example.eraofband.remote.notice.getNotice.GetNoticeView

class NoticeActivity : AppCompatActivity(), GetNoticeView, DeleteNoticeView {
    private lateinit var binding : ActivityNoticeBinding
    private var noticeRVAdapter : NoticeRVAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoticeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.noticeBackIb.setOnClickListener {
            finish()
        }
        binding.noticeMoreIv.setOnClickListener{
            showPopup(binding.noticeMoreIv)
        }
    }
    override fun onResume() {
        super.onResume()
        val noticeService = GetNoticeService()
        noticeService.setNoticeView(this)
        noticeService.getNotice(getJwt()!!, getUserIdx())
    }

    private fun initNoticeRV(list: ArrayList<GetNoticeResult>) {
        noticeRVAdapter = NoticeRVAdapter(list)
        binding.noticeRv.adapter = noticeRVAdapter
        binding.noticeRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
    }

    private fun showPopup(view: View) {
        val themeWrapper = ContextThemeWrapper(applicationContext, R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.notice_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.notice_delete) {
                val noticeDeleteService = DeleteNoticeService()
                noticeDeleteService.setDeleteNoticeView(this)
                noticeDeleteService.deleteNotice(getJwt()!!)
                if (noticeRVAdapter != null)
                    noticeRVAdapter!!.reset()
            }
            false
        }
        popupMenu.show() // 팝업 보여주기
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetSuccess(result: ArrayList<GetNoticeResult>) {
        Log.d("GET/SUCCESS", "$result")
        initNoticeRV(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GET/Failure", "$code $message")
    }

    override fun onDeleteSuccess(result: String) {
        Log.d("DELETE/SUCCESS", result)
    }

    override fun onDeleteFailure(code: Int, message: String) {
        Log.d("DELETE/Failure", "$code $message")
    }
}