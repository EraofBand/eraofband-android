package com.example.eraofband.main.home.session.band

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.FragmentBandRecruitSessionBinding
import com.example.eraofband.main.home.HomeFabDialog

class BandRecruitSessionFragment: Fragment() {

    private var _binding: FragmentBandRecruitSessionBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var volunteerRVAdapter: BandRecruitSessionVolunteerRVAdapter
    private lateinit var sessionRVAdapter: BandRecruitSessionListRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentBandRecruitSessionBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root

    }

    private fun initRecyclerView() {
        // 지원자 목록 리사이클러뷰
        volunteerRVAdapter = BandRecruitSessionVolunteerRVAdapter()
        binding.bandRecruitSessionVolunteerRv.adapter = volunteerRVAdapter
        binding.bandRecruitSessionVolunteerRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

      /*  val bandList = arrayListOf(
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", "")
        )

        volunteerRVAdapter.initVolunteerList(bandList)
*/
        // 세션 모집 리사이클러뷰
        sessionRVAdapter = BandRecruitSessionListRVAdapter()
        binding.bandRecruitSessionRv.adapter = sessionRVAdapter
        binding.bandRecruitSessionRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

       /* val sessionList = arrayListOf(
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", ""),
            Band(R.drawable.band_profile, "", "")
        )

        sessionRVAdapter.initSessionList(sessionList)
*/
        sessionRVAdapter.setMyItemClickListener(object: BandRecruitSessionListRVAdapter.MyItemClickListener{
            override fun showApplyPopup() {
                val applyDialog = SessionApplyDialog()
                applyDialog.isCancelable = false
                applyDialog.show(activity!!.supportFragmentManager, "apply")
            }

        })

    }
}