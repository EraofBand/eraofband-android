package com.example.eraofband.ui.main.usermypage.band

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentUserMypageBandBinding
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserResult
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserService
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserView
import com.example.eraofband.remote.user.getOtherUser.GetUserBand
import com.example.eraofband.ui.main.home.session.band.BandRecruitActivity
import com.example.eraofband.ui.main.mypage.band.MyPageBandRVAdapter
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

class UserMyPageBandFragment : Fragment(), GetOtherUserView {

    private var _binding: FragmentUserMypageBandBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUserMypageBandBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val getOtherUserService = GetOtherUserService()
        getOtherUserService.setOtherUserView(this)
        getOtherUserService.getOtherUser(getJwt()!!, getUserIdx())
    }

    private fun getUserIdx(): Int {  //임시방편입니다요...
        val userIdx = (activity as UserMyPageActivity).otherUserIdx
        return userIdx!!.toInt()
    }

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initRV(item: List<GetUserBand>) {
        if (item.isEmpty()) {  // 비어있으면 비어있다고 메세지를 띄움
            binding.userMypageNoBandTv.visibility = View.VISIBLE
            binding.userMypageBandRv.visibility = View.GONE
            return
        }

        // 비어있지 않으면 리사이클러뷰 연결
        binding.userMypageNoBandTv.visibility = View.GONE
        binding.userMypageBandRv.visibility = View.VISIBLE

        val bandRVAdapter = UserPageBandRVAdapter(context!!)
        binding.userMypageBandRv.adapter = bandRVAdapter
        binding.userMypageBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        bandRVAdapter.initBandList(item)

        bandRVAdapter.setMyItemClickListener(object : UserPageBandRVAdapter.MyItemClickListener {
            override fun onShowDetail(bandIdx: Int) {
                val intent = Intent(activity, BandRecruitActivity::class.java)
                intent.putExtra("bandIdx", bandIdx)

                startActivity(intent)
            }

        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetSuccess(code: Int, result: GetOtherUserResult) {
        Log.d("MYPAGE/SUC", "$result")

        initRV(result.getUserBand)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPAGE/FAIL", "$code $message")
    }

}