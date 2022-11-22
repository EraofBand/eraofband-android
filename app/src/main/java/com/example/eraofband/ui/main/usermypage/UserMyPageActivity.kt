package com.example.eraofband.ui.main.usermypage

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.Resources
import android.graphics.Point
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.MakeChatRoom
import com.example.eraofband.data.Users
import com.example.eraofband.databinding.ActivityUserMypageBinding
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.block.block.BlockService
import com.example.eraofband.remote.block.block.BlockView
import com.example.eraofband.remote.chat.activeChat.ActiveChatService
import com.example.eraofband.remote.chat.activeChat.ActiveChatView
import com.example.eraofband.remote.chat.enterChatRoom.EnterChatRoomResult
import com.example.eraofband.remote.chat.enterChatRoom.EnterChatRoomService
import com.example.eraofband.remote.chat.enterChatRoom.EnterChatRoomView
import com.example.eraofband.remote.chat.isChatRoom.IsChatRoomResult
import com.example.eraofband.remote.chat.isChatRoom.IsChatRoomService
import com.example.eraofband.remote.chat.isChatRoom.IsChatRoomView
import com.example.eraofband.remote.chat.makeChat.MakeChatService
import com.example.eraofband.remote.chat.makeChat.MakeChatView
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserResult
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserService
import com.example.eraofband.remote.user.getOtherUser.GetOtherUserView
import com.example.eraofband.remote.user.userFollow.UserFollowResponse
import com.example.eraofband.remote.user.userFollow.UserFollowService
import com.example.eraofband.remote.user.userFollow.UserFollowView
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowService
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowView
import com.example.eraofband.ui.main.chat.ChatContentActivity
import com.example.eraofband.ui.main.mypage.follow.FollowActivity
import com.example.eraofband.ui.main.report.ReportDialog
import com.example.eraofband.ui.setOnSingleClickListener
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.*

class UserMyPageActivity : AppCompatActivity(), GetOtherUserView, UserFollowView, UserUnfollowView, BlockView, IsChatRoomView, ActiveChatView, MakeChatView, EnterChatRoomView {

    private lateinit var binding: ActivityUserMypageBinding
    internal var otherUserIdx: Int? = null
    private var followerCnt = 0

    private lateinit var nickName: String
    private lateinit var profileImg: String
    private var secondIndex = -1
    private lateinit var chatIdx: String
    private var lastChatIdx = -1

    private val getOtherUserService = GetOtherUserService()
    private val blockService = BlockService()
    private val userUnfollowService = UserUnfollowService() // 언팔로우
    private val userFollowService = UserFollowService() // 팔로우
    private val isChatRoomService = IsChatRoomService()
    private val activeChatService = ActiveChatService()
    private val makeChatService = MakeChatService()
    private val enterChatRoomService = EnterChatRoomService()

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserMypageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent = intent
        otherUserIdx = intent.extras?.getInt("userIdx")!!
        Log.d("USER INDEX", otherUserIdx.toString())

        blockService.setBlockView(this)
        getOtherUserService.setOtherUserView(this)
        userUnfollowService.setUserUnfollowView(this)
        userFollowService.setUserFollowView(this)

    }

    override fun onResume() {
        super.onResume()

        getOtherUserService.getOtherUser(getJwt()!!, otherUserIdx!!)

        isChatRoomService.setChatListView(this)
        activeChatService.setActiveView(this)
        enterChatRoomService.setEnterChatRoomView(this)
        makeChatService.setChatView(this)

        clickListener()
    }

    private fun clickListener() {
        binding.userMypageBackIb.setOnClickListener {
            finish()
        }

        binding.userMypageMoreIv.setOnClickListener { showPopup() }

        // 메세지 클릭하면 채팅방으로 이동
        binding.userMypageMessageTv.setOnSingleClickListener {

            isChatRoomService.isChatRoom(getJwt()!!, Users(getUserIdx(), otherUserIdx!!))

            val intent = Intent(this, ChatContentActivity::class.java)
            intent.putExtra("nickname", nickName)
            intent.putExtra("profile", profileImg)
            intent.putExtra("firstIndex", getUserIdx())
            intent.putExtra("secondIndex", otherUserIdx)
            intent.putExtra("lastChatIdx", lastChatIdx)
            startActivity(intent)
        }

        val userMyPageAdapter = UserMyPageVPAdapter(this)
        binding.userMypageVp.adapter = userMyPageAdapter

        TabLayoutMediator(binding.userMypageTb, binding.userMypageVp) { tab, position ->
            when (position) {
                0 -> tab.text = "포트폴리오"
                1 -> tab.text = "소속 밴드"
            }
        }.attach()

        binding.userMypageFollowTv.setOnSingleClickListener {  // 팔로우 리스트에서 언팔 및 팔로우 시 visibility 변경
            binding.userMypageFollowTv.visibility = View.INVISIBLE
            binding.userMypageUnfollowTv.visibility = View.VISIBLE
            binding.userMypageFollowerCntTv.text = "${followerCnt++}"
            followerCnt += 1

            userFollowService.userFollow(getJwt()!!, otherUserIdx!!)
        }

        binding.userMypageUnfollowTv.setOnSingleClickListener {
            binding.userMypageFollowTv.visibility = View.VISIBLE
            binding.userMypageUnfollowTv.visibility = View.INVISIBLE
            binding.userMypageFollowerCntTv.text = (followerCnt - 1).toString()
            followerCnt -= 1

            userUnfollowService.userUnfollow(getJwt()!!, otherUserIdx!!)
        }

        moveFollowActivity()
    }

    private fun moveFollowActivity() {
        binding.userMypageFollowing.setOnClickListener {
            val intent = Intent(this, FollowActivity::class.java)
            intent.putExtra("nickName", nickName)
            intent.putExtra("current", 0)
            intent.putExtra("userIdx", otherUserIdx)
            startActivity(intent)
        }

        binding.userMypageFollower.setOnClickListener {
            val intent = Intent(this, FollowActivity::class.java)
            intent.putExtra("nickName", nickName)
            intent.putExtra("current", 1)
            intent.putExtra("userIdx", otherUserIdx)
            startActivity(intent)
        }
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    @SuppressLint("SimpleDateFormat")
    private fun setDate() : String {  // 오늘 날짜 불러오기
        val today = System.currentTimeMillis()  // 현재 날짜, 시각 불러오기
        val date = Date(today)
        val mFormat = SimpleDateFormat("yyyy-MM-dd")

        return mFormat.format(date)
    }

    private fun setSession(session : Int) {
        when (session) {
            0 -> binding.userMypageSessionTv.text = "보컬"
            1 -> binding.userMypageSessionTv.text = "기타"
            2 -> binding.userMypageSessionTv.text = "베이스"
            3 -> binding.userMypageSessionTv.text = "드럼"
            else ->  binding.userMypageSessionTv.text = "키보드"
        }
    }

    private fun showPopup() {  // 내 댓글인 경우 삭제 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, binding.userMypageMoreIv, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.user_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.user_report) {
                // 유저 신고하기
                val reportDialog = ReportDialog(getJwt()!!, 0, secondIndex, secondIndex)
                reportDialog.isCancelable = false
                reportDialog.show(supportFragmentManager, "report")
            }
            else {  // 유저 차단하기
                Log.d("BLOCK", "USER")
                blockService.block(getJwt()!!, otherUserIdx!!)
            }

            false
        }

        popupMenu.show() // 팝업 보여주기
    }

    private fun setToast(str : String) {
        val view : View = layoutInflater.inflate(R.layout.toast_signup, findViewById(R.id.toast_signup))
        val toast = Toast(this)

        val text = view.findViewById<TextView>(R.id.toast_signup_text_tv)
        text.text = str

        val display = windowManager.defaultDisplay // in case of Activity
        val size = Point()
        display.getSize(size)  // 상단바 등을 제외한 스크린 전체 크기 구하기
        val height = size.y / 2  // 토스트 메세지가 중간에 고정되어있기 때문에 높이 / 2

        // 중간부터 marginBottom, 버튼 높이 / 2 만큼 빼줌
        toast.view = view
        toast.setGravity(Gravity.FILL_HORIZONTAL, 0, height - 80.toPx())
        toast.show()
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(code: Int, result: GetOtherUserResult) {
        synchronized(this) {
            Log.d("MYPAGE", result.toString())

            Glide.with(this).load(result.getUser.profileImgUrl)
                .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
                .into(binding.userMypageProfileimgIv)
            profileImg = result.getUser.profileImgUrl

            nickName = result.getUser.nickName
            secondIndex = result.getUser.userIdx

            binding.userMypageNicknameTv.text = nickName
            binding.userMypageInfoNicknameTv.text = nickName // 닉네임 연동

            // 디테일한 소개 연동

            val index = result.getUser.region.split(" ")
            val city = index[1]

            val age = setDate().substring(0, 4).toInt() - result.getUser.birth.substring(0, 4).toInt() + 1

            val gender =
                if(result.getUser.gender == "MALE") "남성"
                else "여성"

            binding.userMypageDetailInfoTv.text = "$city | ${age}세 | $gender"

            binding.userMypageIntroductionTv.text = result.getUser.introduction  // 내 소개 연동

            if (binding.userMypageIntroductionTv.lineCount > 3) {
                binding.userMypageLookMoreTv.visibility = View.VISIBLE  // 더보기 표시

                // 더보기 클릭 이벤트
                binding.userMypageLookMoreTv.setOnClickListener {
                    if (binding.userMypageLookMoreTv.text == "더보기") {
                        binding.userMypageLookMoreTv.text = "접기"
                        binding.userMypageIntroductionTv.maxLines = 100
                    }
                    else {
                        binding.userMypageLookMoreTv.text = "더보기"
                        binding.userMypageIntroductionTv.maxLines = 3
                    }
                }
            }

            // 숫자 연동
            binding.userMypageFollowingCntTv.text = result.getUser.followerCount.toString()
            binding.userMypageFollowerCntTv.text = result.getUser.followeeCount.toString()
            followerCnt = result.getUser.followeeCount
            binding.userMypagePortfolioCntTv.text = result.getUser.pofolCount.toString()

            setSession(result.getUser.userSession)  // 세션 연동

            if (result.getUser.follow == 0){
                binding.userMypageFollowTv.visibility = View.VISIBLE
                binding.userMypageUnfollowTv.visibility = View.INVISIBLE
            } else {
                binding.userMypageFollowTv.visibility = View.INVISIBLE
                binding.userMypageUnfollowTv.visibility = View.VISIBLE
            }
        }
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("USER MYPAGE / FAIL", "$code $message")
    }

    // 팔로우 리스폰스----------------------------------------------------------------
    override fun onUserFollowSuccess(code: Int, response: UserFollowResponse) {
        Log.d("USER FOLLOW / SUCCESS", "코드 : $code / 응답 : $response")
    }

    override fun onUserFollowFailure(code: Int, message: String) {
        Log.d("USER FOLLOW / FAIL", "$code $message")
    }

    // 언팔로우 리스폰스-------------------------------------------------------------------
    override fun onUserUnfollowSuccess(code: Int, response: BasicResponse) {
        Log.d("USER UNFOLLOW / SUCCESS", "코드 : $code / 응답 : $response")
    }

    override fun onUserUnfollowFailure(code: Int, message: String) {
        Log.d("USER UNLLOW / FAIL", "$code $message")
    }

    override fun onBlockSuccess(result: String) {
        Log.d("BLOCK/SUC", result)

        setToast("차단이 완료되었습니다.")
    }

    override fun onBlockFailure(code: Int, message: String) {
        Log.d("BLOCK/FAIL", "$code $message")
    }


    // 채팅방 활성화 API
    override fun onActiveSuccess(result: String) {
        Log.d("ACTIVE CHATROOM / SUCCESS", result)
    }

    override fun onActiveFailure(code: Int, message: String) {
        Log.d("ACTIVE CHATROOM / FAIL", "$code $message")
    }

    // 채팅방 존재 확인 API
    override fun onExistsSuccess(result: IsChatRoomResult) {
        Log.d("EXIST CHATROOM / SUCCESS", result.toString())

        if(result.chatRoomIdx == "null"){
            chatIdx = "${UUID.randomUUID()}"
            makeChatService.makeChat(MakeChatRoom(chatIdx, getUserIdx(), otherUserIdx!!))
        } else{
            chatIdx = result.chatRoomIdx
            if(result.status == 0){
                activeChatService.activeChat(getJwt()!!, MakeChatRoom(result.chatRoomIdx, getUserIdx(), otherUserIdx!!))
            }
            enterChatRoomService.enterChatRoom(getJwt()!!, chatIdx)
        }
    }

    override fun onExistsFailure(code: Int, message: String) {
        Log.d("EXIST CHATROOM / FAIL", "$code $message")
    }


    // 채팅방 만들기 API
    override fun onMakeSuccess(result: String) {
        Log.d("MAKE CHATROOM / SUCCESS", result)
    }

    override fun onMakeFailure(code: Int, message: String) {
        Log.d("MAKE CHATROOM / FAIL", "$code $message")
    }


    // 채팅방 들어가기 API
    override fun onEnterSuccess(result: EnterChatRoomResult) {
        Log.d("ENTER CHATROOM / SUCCESS", result.toString())
        lastChatIdx = result.lastChatIdx
    }

    override fun onEnterFailure(code: Int, message: String) {
        Log.d("ENTER CHATROOM / FAIL", "$code $message")
    }

}