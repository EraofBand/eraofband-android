package com.example.eraofband.ui.main.home.session.band

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.bumptech.glide.Glide
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityBandRecruitBinding
import com.example.eraofband.remote.band.deleteBand.DeleteBandResponse
import com.example.eraofband.remote.band.deleteBand.DeleteBandService
import com.example.eraofband.remote.band.deleteBand.DeleteBandView
import com.example.eraofband.remote.band.deleteUserBand.DeleteUserBandResponse
import com.example.eraofband.remote.band.deleteUserBand.DeleteUserBandService
import com.example.eraofband.remote.band.deleteUserBand.DeleteUserBandView
import com.example.eraofband.remote.band.bandLike.BandLikeResult
import com.example.eraofband.remote.band.bandLike.BandLikeService
import com.example.eraofband.remote.band.bandLike.BandLikeView
import com.example.eraofband.remote.band.getBand.GetBandResult
import com.example.eraofband.remote.band.getBand.GetBandService
import com.example.eraofband.remote.band.getBand.GetBandView
import com.example.eraofband.remote.band.getBand.SessionMembers
import com.google.android.material.tabs.TabLayoutMediator
import com.google.gson.Gson

class BandRecruitActivity: AppCompatActivity(), GetBandView, BandLikeView, DeleteBandView, DeleteUserBandView {

    private lateinit var binding: ActivityBandRecruitBinding

    private val gson = Gson()

    private var like = false

    private var bandIdx = 0
    private var leaderIdx = 0

    private var bandMember = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBandRecruitBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bandIdx = intent.getIntExtra("bandIdx", 0)

        binding.homeBandRecruitBackIv.setOnClickListener { finish() }  // 뒤로가기

        binding.homeBandRecruitListIv.setOnClickListener {
            showPopup(binding.homeBandRecruitListIv)
        }

        binding.homeBandRecruitRl.setOnRefreshListener {
            val bandService = GetBandService()
            bandService.setBandView(this)
            bandService.getBand(getJwt()!!, intent.getIntExtra("bandIdx", 0))

            binding.homeBandRecruitRl.isRefreshing = false
        }

        val likeService = BandLikeService()
        likeService.setLikeView(this)

        binding.homeBandRecruitLikeIv.setOnClickListener {
            Log.d("LIKETEST", like.toString())
            if (like) likeService.deleteLike(
                getJwt()!!,
                intent.getIntExtra("bandIdx", 0)
            )  // 좋아요 취소 처리
            else likeService.like(getJwt()!!, intent.getIntExtra("bandIdx", 0))  // 좋아요 처리
        }
    }

    override fun onResume() {
        super.onResume()

        val bandService = GetBandService()
        bandService.setBandView(this)
        bandService.getBand(getJwt()!!, intent.getIntExtra("bandIdx", 0))
    }

    private fun initViewPager() {
        val bandRecruitVPAdapter = BandRecruitVPAdapter(this)
        binding.homeBandRecruitVp.adapter = bandRecruitVPAdapter

        TabLayoutMediator(binding.homeBandRecruitTb, binding.homeBandRecruitVp) { tab, position ->
            when (position) {
                0 -> tab.text = "밴드 소개"
                1 -> tab.text = "세션 모집"
                2 -> tab.text = "앨범"
            }
        }.attach()
    }

    private fun getJwt(): String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetSuccess(result: GetBandResult) {
        Log.d("GETBAND/SUC", result.toString())
        // 밴드 정보 연동
        binding.homeBandRecruitBandTitleTv.text = result.bandTitle  // 밴드 이름 연동

        Glide.with(this).load(result.bandImgUrl)
            .into(binding.homeBandRecruitBandImgIv)  // 밴드 이미지 연동
        binding.homeBandRecruitBandImgIv.clipToOutline = true  // 모서리 깎기

        binding.homeBandRecruitBandNameTv.text = result.bandTitle  // 밴드 이름 연동
        binding.homeBandRecruitBandIntroTv.text = result.bandIntroduction  // 한줄 소개 연동
        binding.homeBandRecruitCntTv.text = "${result.memberCount} / ${result.capacity}"  // 멤버 수

        leaderIdx = result.userIdx
        bandMember = checkUserIdx(result.sessionMembers)

        // 좋아요 여부 연동
        if (result.likeOrNot == "Y") {
            like = true
            binding.homeBandRecruitLikeIv.setImageResource(R.drawable.ic_heart_on)
        } else {
            like = false
            binding.homeBandRecruitLikeIv.setImageResource(R.drawable.ic_heart_off)
        }

        // viewPager로 데이터를 넘기기 위해 저장
        val bandSP = getSharedPreferences("band", MODE_PRIVATE)
        val bandEdit = bandSP.edit()

        val bandJson = gson.toJson(result)
        bandEdit.putString("bandInfo", bandJson)
        bandEdit.apply()

        initViewPager()  // 뷰페이저 연결
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GETBAND/FAIL", "$code $message")
    }

    override fun onLikeSuccess(result: BandLikeResult) {
        Log.d("LIKE/SUC", "$result")
        // 좋아요 성공하면 하트 채워주기
        binding.homeBandRecruitLikeIv.setImageResource(R.drawable.ic_heart_on)
        like = true
    }

    override fun onLikeFailure(code: Int, message: String) {
        Log.d("LIKE/FAIL", "$code $message")
    }

    override fun onDeleteLikeSuccess(result: String) {
        Log.d("DELETELIKE/SUC", result)
        // 좋아요 취소 성공하면 하트 원래대로 돌려주기
        binding.homeBandRecruitLikeIv.setImageResource(R.drawable.ic_heart_off)
        like = false
    }

    override fun onDeleteLikeFailure(code: Int, message: String) {
        Log.d("DELETELIKE/FAIL", "$code $message")
    }

    private fun getUserIdx(): Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun showPopup(view: View) {
        val themeWrapper = ContextThemeWrapper(applicationContext, R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.band_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.band_edit) {
                val intent = Intent(this, BandEditActivity::class.java)
                intent.putExtra("bandIdx", bandIdx)

                startActivity(intent)
            } else if (item!!.itemId == R.id.band_delete) {
                // 밴드 삭제
                val deleteBandService = DeleteBandService()
                deleteBandService.setDeleteView(this)
                deleteBandService.deleteBand(getJwt()!!, bandIdx, getUserIdx())
            } else {
                //밴드 탈퇴
                val deleteUserBandService = DeleteUserBandService()
                deleteUserBandService.setDeleteView(this)
                deleteUserBandService.deleteUserBand(getJwt()!!, bandIdx)
            }
            false
        }

        if(getUserIdx() == leaderIdx){
            popupMenu.menu.setGroupVisible(R.id.band_report_gr, false)
            popupMenu.menu.setGroupVisible(R.id.band_leave_gr, false)
        } else if(bandMember) {
            popupMenu.menu.setGroupVisible(R.id.band_report_gr, false)
            popupMenu.menu.setGroupVisible(R.id.band_edit_gr, false)
            popupMenu.menu.setGroupVisible(R.id.band_delete_gr, false)
        } else{
            popupMenu.menu.setGroupVisible(R.id.band_leave_gr, false)
            popupMenu.menu.setGroupVisible(R.id.band_edit_gr, false)
            popupMenu.menu.setGroupVisible(R.id.band_delete_gr, false)
        }

        popupMenu.show() // 팝업 보여주기
    }

    private fun checkUserIdx(memberList: List<SessionMembers>): Boolean {
        // 만약 내 userIdx가 멤버 리스트의 userIdx와 같으면 밴드 멤버에 속함
        if(memberList.isEmpty()) return false  // 밴드 멤버가 없으면 무조건 false

        for(element in memberList) {
            if(getUserIdx() == element.userIdx) return true
        }

        return false
    }

    override fun onDeleteSuccess(code: Int, result: String) {
        Log.d("DELETE BAND / SUCCESS", result)
    }

    override fun onDeleteFailure(response: DeleteBandResponse) {
        Log.d("DELETE BAND / FAIL", response.toString())
    }

    override fun onDeleteUserSuccess(code: Int, result: String) {
        Log.d("DELETE BAND / SUCCESS", result)
    }

    override fun onDeleteUserFailure(response: DeleteUserBandResponse) {
        Log.d("DELETE BAND / FAIL", response.toString())
    }
}
