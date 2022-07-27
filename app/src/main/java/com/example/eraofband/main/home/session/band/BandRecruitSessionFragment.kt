package com.example.eraofband.main.home.session.band

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.data.SessionList
import com.example.eraofband.databinding.FragmentBandRecruitSessionBinding
import com.example.eraofband.main.home.HomeFabDialog
import com.example.eraofband.remote.getBand.Applicants
import com.example.eraofband.remote.getBand.GetBandResult
import com.google.gson.Gson

class BandRecruitSessionFragment: Fragment() {

    private var _binding: FragmentBandRecruitSessionBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var volunteerRVAdapter: BandRecruitSessionVolunteerRVAdapter
    private lateinit var sessionRVAdapter: BandRecruitSessionListRVAdapter

    private val gson = Gson()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBandRecruitSessionBinding.inflate(inflater, container, false)

        return binding.root

    }

    override fun onResume() {
        super.onResume()

        val bandSP = requireActivity().getSharedPreferences("band", Context.MODE_PRIVATE)
        val bandJson = bandSP.getString("bandInfo", "")

        val bandInfo = gson.fromJson(bandJson, GetBandResult::class.java)

        initInfo(bandInfo)
    }

    private fun initInfo(band: GetBandResult) {
        if(getUserIdx() == band.userIdx) {  // 내가 밴드를 만든 사람인 경우
            binding.bandRecruitSessionVolunteer.visibility = View.VISIBLE

            if(band.applicants.isEmpty()) {  // 지원자가 없는 경우
                binding.bandRecruitSessionVolunteerRv.visibility = View.GONE
                binding.bandRecruitSessionNoVolunteerTv.visibility = View.VISIBLE
            }
            else {  // 지원자가 있는 경우
                binding.bandRecruitSessionVolunteerRv.visibility = View.VISIBLE
                binding.bandRecruitSessionNoVolunteerTv.visibility = View.GONE

                initApplicantRV(band.applicants)
            }
        }
        else {
            binding.bandRecruitSessionVolunteer.visibility = View.GONE
        }

        initSessionRV(band)
    }

    private fun getUserIdx() : Int {
        val userSP = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun initApplicantRV(applyItem: List<Applicants>) {
        // 지원자 목록 리사이클러뷰
        volunteerRVAdapter = BandRecruitSessionVolunteerRVAdapter(context!!)
        binding.bandRecruitSessionVolunteerRv.adapter = volunteerRVAdapter
        binding.bandRecruitSessionVolunteerRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        volunteerRVAdapter.initVolunteerList(applyItem)

        volunteerRVAdapter.setMyItemClickListener(object: BandRecruitSessionVolunteerRVAdapter.MyItemClickListener{
            override fun onShowDecisionPopup(code: String) {
                val applyDialog = SessionApplyDialog(code)
                applyDialog.isCancelable = false
                applyDialog.show(activity!!.supportFragmentManager, "applicant")
            }

        })
    }

    private fun initSessionRV(band: GetBandResult) {
        // 세션 모집 리사이클러뷰
        sessionRVAdapter = BandRecruitSessionListRVAdapter(band.bandTitle)
        binding.bandRecruitSessionRv.adapter = sessionRVAdapter
        binding.bandRecruitSessionRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        if(band.vocal > 0) sessionRVAdapter.addSession(SessionList("보컬", band.vocal, band.vocalComment))
        if(band.guitar > 0) sessionRVAdapter.addSession(SessionList("기타", band.guitar, band.guitarComment))
        if(band.base > 0) sessionRVAdapter.addSession(SessionList("베이스", band.base, band.baseComment))
        if(band.keyboard > 0) sessionRVAdapter.addSession(SessionList("키보드", band.keyboard, band.keyboardComment))
        if(band.drum > 0) sessionRVAdapter.addSession(SessionList("드럼", band.drum, band.drumComment))

        sessionRVAdapter.setMyItemClickListener(object: BandRecruitSessionListRVAdapter.MyItemClickListener{
            override fun showApplyPopup(code: String) {
                val applyDialog = SessionApplyDialog(code)
                applyDialog.isCancelable = false
                applyDialog.show(activity!!.supportFragmentManager, "apply")
            }
        })

    }
}