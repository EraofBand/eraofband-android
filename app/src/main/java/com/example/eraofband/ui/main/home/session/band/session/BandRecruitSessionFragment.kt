package com.example.eraofband.ui.main.home.session.band.session

import android.content.ActivityNotFoundException
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.data.SessionInfo
import com.example.eraofband.data.SessionList
import com.example.eraofband.databinding.FragmentBandRecruitSessionBinding
import com.example.eraofband.remote.band.getBand.Applicants
import com.example.eraofband.remote.band.getBand.GetBandResult
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import com.google.gson.Gson
import com.kakao.sdk.common.util.KakaoCustomTabsClient
import com.kakao.sdk.share.ShareClient
import com.kakao.sdk.share.WebSharerClient
import com.kakao.sdk.template.model.Button
import com.kakao.sdk.template.model.Content
import com.kakao.sdk.template.model.FeedTemplate
import com.kakao.sdk.template.model.Link

class BandRecruitSessionFragment : Fragment() {

    private var _binding: FragmentBandRecruitSessionBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var volunteerRVAdapter: BandRecruitSessionVolunteerRVAdapter
    private lateinit var sessionRVAdapter: BandRecruitSessionListRVAdapter

    private var sessionInfo = SessionInfo(0, "", 0, "", 0, "", 0, "", 0, "")

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

        val bandSP = requireActivity().getSharedPreferences("band", MODE_PRIVATE)
        val bandJson = bandSP.getString("bandInfo", "")

        val bandInfo = gson.fromJson(bandJson, GetBandResult::class.java)

        synchronized(this) {
            initInfo(bandInfo)
        }
    }

    private fun initInfo(band: GetBandResult) {
        // 세션 정보 저장
        sessionInfo = SessionInfo(
            band.vocal,
            band.vocalComment,
            band.guitar,
            band.guitarComment,
            band.base,
            band.baseComment,
            band.keyboard,
            band.keyboardComment,
            band.drum,
            band.drumComment
        )

        if (getUserIdx() == band.userIdx) {  // 내가 밴드를 만든 사람인 경우
            binding.bandRecruitSessionVolunteer.visibility = View.VISIBLE

            if (band.applicants.isEmpty()) {  // 지원자가 없는 경우
                binding.bandRecruitSessionVolunteerRv.visibility = View.GONE
                binding.bandRecruitSessionNoVolunteerTv.visibility = View.VISIBLE
            } else {  // 지원자가 있는 경우
                binding.bandRecruitSessionVolunteerRv.visibility = View.VISIBLE
                binding.bandRecruitSessionNoVolunteerTv.visibility = View.GONE

                initApplicantRV(band.applicants, band.bandIdx)
            }
        } else {
            binding.bandRecruitSessionVolunteer.visibility = View.GONE
        }

        initSessionRV(band)
    }

    private fun getUserIdx(): Int {
        val userSP = requireActivity().getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initApplicantRV(applyItem: List<Applicants>, bandIdx: Int) {
        // 지원자 목록 리사이클러뷰
        volunteerRVAdapter = BandRecruitSessionVolunteerRVAdapter(requireContext(), bandIdx)
        binding.bandRecruitSessionVolunteerRv.adapter = volunteerRVAdapter
        binding.bandRecruitSessionVolunteerRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        volunteerRVAdapter.initVolunteerList(applyItem)

        volunteerRVAdapter.setMyItemClickListener(object :
            BandRecruitSessionVolunteerRVAdapter.MyItemClickListener {
            override fun onShowDecisionPopup(bandIdx: Int, applicant: Applicants, position: Int) {
                val applyDialog = SessionDecisionDialog(bandIdx, applicant)
                applyDialog.show(activity!!.supportFragmentManager, "applicant")

                // 지원 수락, 거절 리스너
                applyDialog.setDialogListener(object : SessionDecisionDialog.ApplyDecision {
                    override fun applyAccept(session: Int) {  // 지원 수락
                        // 수락 다이얼로그 띄우기
                        val completeDialog = SessionCompleteDialog()
                        completeDialog.isCancelable = false
                        completeDialog.show(activity!!.supportFragmentManager, "acceptApply")

                        applyDialog.dismiss()  // 창 제거

                        // 확인 누르면 지원자 목록에서 삭제
                        completeDialog.setDialogListener(object :
                            SessionCompleteDialog.ApplyDecision {
                            override fun applyAccept() {  // 수락만 사용
                                volunteerRVAdapter.deleteVolunteer(position)
                                reduceCnt(session)
                            }

                            override fun applyReject() {
                                volunteerRVAdapter.deleteVolunteer(position)
                            }
                        })
                    }

                    override fun applyReject(session: Int) {  // 지원 거절
                        // 거절 다이얼로그 띄우기
                        val completeDialog = SessionCompleteDialog()
                        completeDialog.isCancelable = false
                        completeDialog.show(activity!!.supportFragmentManager, "rejectApply")

                        applyDialog.dismiss()  // 창 제거

                        // 확인 누르면 지원자 목록에서 삭제
                        completeDialog.setDialogListener(object :
                            SessionCompleteDialog.ApplyDecision {
                            override fun applyAccept() {
                                volunteerRVAdapter.deleteVolunteer(position)
                            }

                            override fun applyReject() {  // 거절만 사용
                                volunteerRVAdapter.deleteVolunteer(position)
                            }
                        })
                    }

                })
            }

            override fun onShowUserPage(userIdx: Int) {
                if (userIdx == getUserIdx()) {
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

    private fun initSessionRV(band: GetBandResult) {
        // 세션 모집 리사이클러뷰
        sessionRVAdapter = BandRecruitSessionListRVAdapter(band.bandTitle, band.bandIdx)
        binding.bandRecruitSessionRv.adapter = sessionRVAdapter
        binding.bandRecruitSessionRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        if (band.vocal > 0) sessionRVAdapter.addSession(
            SessionList(
                0,
                "보컬",
                band.vocal,
                band.vocalComment
            )
        )
        if (band.guitar > 0) sessionRVAdapter.addSession(
            SessionList(
                1,
                "기타",
                band.guitar,
                band.guitarComment
            )
        )
        if (band.base > 0) sessionRVAdapter.addSession(
            SessionList(
                2,
                "베이스",
                band.base,
                band.baseComment
            )
        )
        if (band.keyboard > 0) sessionRVAdapter.addSession(
            SessionList(
                3,
                "키보드",
                band.keyboard,
                band.keyboardComment
            )
        )
        if (band.drum > 0) sessionRVAdapter.addSession(
            SessionList(
                4,
                "드럼",
                band.drum,
                band.drumComment
            )
        )

        sessionRVAdapter.setMyItemClickListener(object :
            BandRecruitSessionListRVAdapter.MyItemClickListener {
            override fun showApplyPopup(bandIdx: Int, session: Int) {
                if(band.userIdx == getUserIdx()) {
                    Toast.makeText(context, "자신이 생성한 밴드에는 지원할 수 없습니다.", Toast.LENGTH_SHORT).show()
                } else {
                    val applyDialog = SessionApplyDialog(getJwt()!!, session, bandIdx)
                    applyDialog.show(activity!!.supportFragmentManager, "apply")
                }
            }

            override fun shareRecruitSession(
                bandIdx: Int,
                session: String,
                sessionComment: String
            ) {
                var defaultFeed: FeedTemplate = FeedTemplate(
                    content = Content(
                        title = "${band.bandTitle} $session 모집",
                        description = sessionComment,
                        imageUrl = band.bandImgUrl,
                        link = Link(
                            mobileWebUrl = "https://play.google.com"
                        )
                    ),
                    buttons = listOf(
                        Button(
                            "앱으로 보기",
                            Link(
                                androidExecutionParams = mapOf("test" to "test"),
                                iosExecutionParams = mapOf("test" to "test")
                            )
                        )
                    )
                )
                // 피드 메시지 보내기
                // 카카오톡 설치여부 확인
                if (ShareClient.instance.isKakaoTalkSharingAvailable(requireContext())) {
                    // 카카오톡으로 카카오톡 공유 가능
                    ShareClient.instance.shareDefault(requireContext(), defaultFeed) { sharingResult, error ->
                        if (error != null) {
                            Log.e("SHARE", "카카오톡 공유 실패", error)
                        }
                        else if (sharingResult != null) {
                            Log.d("SHARE", "카카오톡 공유 성공 ${sharingResult.intent}")
                            startActivity(sharingResult.intent)

                            // 카카오톡 공유에 성공했지만 아래 경고 메시지가 존재할 경우 일부 컨텐츠가 정상 동작하지 않을 수 있습니다.
                            Log.w("SHARE", "Warning Msg: ${sharingResult.warningMsg}")
                            Log.w("SHARE", "Argument Msg: ${sharingResult.argumentMsg}")
                        }
                    }
                } else {
                    // 카카오톡 미설치: 웹 공유 사용 권장
                    // 웹 공유 예시 코드
                    val sharerUrl = WebSharerClient.instance.makeDefaultUrl(defaultFeed)

                    // CustomTabs으로 웹 브라우저 열기

                    // 1. CustomTabsServiceConnection 지원 브라우저 열기
                    // ex) Chrome, 삼성 인터넷, FireFox, 웨일 등
                    try {
                        KakaoCustomTabsClient.openWithDefault(requireContext(), sharerUrl)
                    } catch(e: UnsupportedOperationException) {
                        // CustomTabsServiceConnection 지원 브라우저가 없을 때 예외처리
                    }

                    // 2. CustomTabsServiceConnection 미지원 브라우저 열기
                    // ex) 다음, 네이버 등
                    try {
                        KakaoCustomTabsClient.open(requireContext(), sharerUrl)
                    } catch (e: ActivityNotFoundException) {
                        // 디바이스에 설치된 인터넷 브라우저가 없을 때 예외처리
                    }
                }
            }
        })
    }

    private fun reduceCnt(session: Int) {
        val sessionArray = arrayListOf(
            sessionInfo.vocal,
            sessionInfo.guitar,
            sessionInfo.base,
            sessionInfo.keyboard,
            sessionInfo.drum
        )
        var position = 0

        for (i in 0 until sessionArray.size) {  // 지원자를 모집한 세션의 위치를 찾음
            if (i == session) break  // 모집한 세션을 찾으면 종료
            if (sessionArray[i] != 0) position++  // 모집 인원이 있으면 adapter도 있을 거라고 생각
        }

        when (session) {
            0 -> {
                sessionInfo.vocal -= 1
                if (sessionInfo.vocal == 0) {  // 모집이 끝나면 제거
                    sessionRVAdapter.deleteSession(position)
                    return
                }
                // 정보 변경
                sessionRVAdapter.modifySession(
                    position,
                    SessionList(session, "보컬", sessionInfo.vocal, sessionInfo.vocalComment)
                )
            }
            1 -> {
                sessionInfo.guitar -= 1
                if (sessionInfo.guitar == 0) {  // 모집이 끝나면 제거
                    sessionRVAdapter.deleteSession(position)
                    return
                }

                sessionRVAdapter.modifySession(
                    position,
                    SessionList(session, "기타", sessionInfo.guitar, sessionInfo.guitarComment)
                )
            }
            2 -> {
                sessionInfo.base -= 1
                if (sessionInfo.base == 0) {  // 모집이 끝나면 제거
                    sessionRVAdapter.deleteSession(position)
                    return
                }

                sessionRVAdapter.modifySession(
                    position,
                    SessionList(session, "베이스", sessionInfo.base, sessionInfo.baseComment)
                )
            }
            3 -> {
                sessionInfo.keyboard -= 1
                if (sessionInfo.keyboard == 0) {  // 모집이 끝나면 제거
                    sessionRVAdapter.deleteSession(position)
                    return
                }

                sessionRVAdapter.modifySession(
                    position,
                    SessionList(session, "키보드", sessionInfo.keyboard, sessionInfo.keyboardComment)
                )
            }
            else -> {
                sessionInfo.drum -= 1
                if (sessionInfo.drum == 0) {  // 모집이 끝나면 제거
                    sessionRVAdapter.deleteSession(position)
                    return
                }

                sessionRVAdapter.modifySession(
                    position,
                    SessionList(session, "드럼", sessionInfo.drum, sessionInfo.drumComment)
                )
            }
        }

        // 지원가가 더 없으면 지원자가 없습니다 문구를 띄워줌
        if (volunteerRVAdapter.itemCount == 0) binding.bandRecruitSessionNoVolunteerTv.visibility =
            View.VISIBLE
    }
}