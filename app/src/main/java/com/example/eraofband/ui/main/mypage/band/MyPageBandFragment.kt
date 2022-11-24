package com.example.eraofband.ui.main.mypage.band

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentMypageBandBinding
import com.example.eraofband.remote.user.getMyPage.GetMyPageResult
import com.example.eraofband.remote.user.getMyPage.GetMyPageService
import com.example.eraofband.remote.user.getMyPage.GetMyPageView
import com.example.eraofband.remote.user.getMyPage.GetUserBand
import com.example.eraofband.ui.main.home.session.band.BandRecruitActivity

class MyPageBandFragment : Fragment(), GetMyPageView {
    private var _binding: FragmentMypageBandBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMypageBandBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()

        val getMyPageBand = GetMyPageService()
        getMyPageBand.setUserView(this)
        getMyPageBand.getMyInfo(getJwt()!!, getUserIdx())
    }

    private fun getUserIdx(): Int {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initRV(item: List<GetUserBand>) {
        if (item.isEmpty()) {  // 비어있으면 비어있다고 메세지를 띄움
            binding.mypageNoBandTv.visibility = View.VISIBLE
            binding.mypageBandRv.visibility = View.GONE
            return
        }

        // 비어있지 않으면 리사이클러뷰 연결
        binding.mypageNoBandTv.visibility = View.GONE
        binding.mypageBandRv.visibility = View.VISIBLE

        val bandRVAdapter = MyPageBandRVAdapter(requireContext())
        binding.mypageBandRv.adapter = bandRVAdapter
        binding.mypageBandRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        bandRVAdapter.initBandList(item)

        bandRVAdapter.setMyItemClickListener(object : MyPageBandRVAdapter.MyItemClickListener {
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

    override fun onGetSuccess(code: Int, result: GetMyPageResult) {
        Log.d("MYPAGE/SUC", "$result")

        initRV(result.getUserBand)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("MYPAGE/FAIL", "$code $message")
    }
}