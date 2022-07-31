package com.example.eraofband.main.home.session.band

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentBandRecruitInfoBinding
import com.example.eraofband.main.mypage.MyPageActivity
import com.example.eraofband.main.usermypage.UserMyPageActivity
import com.example.eraofband.remote.getBand.GetBandResult
import com.example.eraofband.remote.getBand.SessionMembers
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs

class BandRecruitInfoFragment: Fragment() {

    private var _binding: FragmentBandRecruitInfoBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var bandMemberRVAdapter: BandMemberRVAdapter

    private val gson = Gson()
    private var leaderIdx = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBandRecruitInfoBinding.inflate(inflater, container, false)

        // 리더 정보 보기
        binding.bandRecruitInfoLeaderProfileIv.setOnClickListener {
            if(leaderIdx == getUserIdx()) {
                startActivity(Intent(activity, MyPageActivity::class.java))
            }  // 만약 누른 유저가 본인일 경우
            else {
                val intent = Intent(activity, UserMyPageActivity::class.java)
                intent.putExtra("userIdx", leaderIdx)
                startActivity(intent)
            }  // 다른 유저일 경우
        }

        // 리더 정보 보기
        binding.bandRecruitInfoLeaderNicknameTv.setOnClickListener {
            if(leaderIdx == getUserIdx()) {
                startActivity(Intent(activity, MyPageActivity::class.java))
            }  // 만약 누른 유저가 본인일 경우
            else {
                val intent = Intent(activity, UserMyPageActivity::class.java)
                intent.putExtra("userIdx", leaderIdx)
                startActivity(intent)
            }  // 다른 유저일 경우
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val bandSP = requireActivity().getSharedPreferences("band", MODE_PRIVATE)
        val bandJson = bandSP.getString("bandInfo", "")

        val bandInfo = gson.fromJson(bandJson, GetBandResult::class.java)

        initInfo(bandInfo)

        Log.d("BANDINFO", bandInfo.toString())
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun initInfo(band: GetBandResult) {
        if(band.performDate.isNullOrEmpty()) binding.bandRecruitInfoShowTv.text = "미정"  // 공연 여부
        else binding.bandRecruitInfoShowTv.text = "확정"
        binding.bandRecruitInfoAreaTv.text = band.bandRegion  // 활동 지역

        binding.bandRecruitInfoLeaderNicknameTv.text = band.nickName  // 리더 닉네임
        binding.bandRecruitInfoLeaderIntroTv.text = band.userIntroduction  // 리더 소개
        leaderIdx = band.userIdx

        binding.bandRecruitInfoBandIntroTv.text = band.bandContent  // 밴드 소개

        if(band.sessionMembers.isEmpty()) {  // 밴드 멤버가 없는 경우
            binding.bandRecruitInfoMemberRv.visibility = View.GONE
            binding.bandRecruitInfoNoMemberTv.visibility = View.VISIBLE
        }
        else {  // 밴드 멤버가 있는 경우
            binding.bandRecruitInfoMemberRv.visibility = View.VISIBLE
            binding.bandRecruitInfoNoMemberTv.visibility = View.GONE

            initRecyclerView(band.sessionMembers)  // 세션 멤버
        }

        if(!band.performDate.isNullOrEmpty() || !band.performTime.isNullOrEmpty() || !band.performLocation.isNullOrEmpty()) {
            // 공연 정보가 하나라도 있는 경우
            binding.bandRecruitInfoShowCl.visibility = View.VISIBLE

            binding.bandRecruitInfoShowLeftTv.text = "D${checkLeftDay(band.performDate!!)}"

            binding.bandRecruitInfoShowTitleTv.text = "${band.bandTitle} 정기공연"

            binding.bandRecruitInfoShowCalenderTv.text = band.performDate  // 공연 날짜
            binding.bandRecruitInfoShowTimeTv.text = band.performTime  // 공연 시간
            binding.bandRecruitInfoShowLocationTv.text = band.performLocation  // 공연 지역

            if(band.performFee == 0) binding.bandInfoShowTicketTv.text = "무료"  // 공연 가격
            else binding.bandInfoShowTicketTv.text = band.performFee.toString()

        }
        else {
            binding.bandRecruitInfoShowCl.visibility = View.GONE
            binding.bandRecruitInfoNoPerformTv.visibility = View.VISIBLE
        }

        // 채팅방 링크
        if(checkUserIdx(band.userIdx, band.sessionMembers)) binding.bandRecruitInfoChatTv.text = band.chatRoomLink  // 내가 밴드 멤버라면
        else binding.bandRecruitInfoChatTv.text = "밴드 멤버에게만 공개됩니다"  // 밴드 멤버가 아니라면

    }

    private fun initRecyclerView(item: List<SessionMembers>) {
        bandMemberRVAdapter = BandMemberRVAdapter(context!!)

        binding.bandRecruitInfoMemberRv.adapter = bandMemberRVAdapter
        binding.bandRecruitInfoMemberRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        bandMemberRVAdapter.initMemberList(item)

        bandMemberRVAdapter.setMyItemClickListener(object : BandMemberRVAdapter.MyItemClickListener {
            override fun onShowUserPage(userIdx: Int) {
                if(userIdx == getUserIdx()) {
                    startActivity(Intent(activity, MyPageActivity::class.java))
                }  // 만약 누른 유저가 본인일 경우
                else {
                    val intent = Intent(activity, UserMyPageActivity::class.java)
                    intent.putExtra("userIdx", userIdx)
                    startActivity(intent)
                }  // 다른 유저일 경우
            }
        })

    }

    private fun checkLeftDay(performDate: String): String {

        val dateFormat = SimpleDateFormat("yyyyMMdd")

        val endDate = dateFormat.parse(performDate.replace("-", ""))!!.time
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time.time

        val date = ((endDate - today) / (24 * 60 * 60 * 1000)).toInt()

        return if(date == 0) "D-Day"  // 당일인 경우
        else if(date < 0) "+${abs(date)}"  // 이미 지난 경우
        else "-${abs(date)}"  // 아직 안지난 경우
    }

    private fun checkUserIdx(userIdx: Int, memberList: List<SessionMembers>): Boolean {
        // 만약 내 userIdx가 멤버 리스트의 userIdx와 같으면 밴드 멤버에 속함
        if(getUserIdx() == userIdx) return true  // 내가 밴드를 만든 사람이면 무조건 true

        if(memberList.isEmpty()) return false  // 밴드를 만든 사람이 아닌데 밴드 멤버가 없으면 무조건 false

        for(element in memberList) {
            if(getUserIdx() == element.userIdx) return true
        }

        return false
    }
}