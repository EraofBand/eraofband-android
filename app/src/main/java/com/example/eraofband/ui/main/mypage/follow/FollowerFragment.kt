package com.example.eraofband.ui.main.mypage.follow

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentFollowerBinding
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtService
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtView
import com.example.eraofband.remote.user.refreshjwt.RefreshResult
import com.example.eraofband.remote.user.userFollow.UserFollowResponse
import com.example.eraofband.remote.user.userFollow.UserFollowService
import com.example.eraofband.remote.user.userFollow.UserFollowView
import com.example.eraofband.remote.user.userFollowList.FollowerInfo
import com.example.eraofband.remote.user.userFollowList.UserFollowListResult
import com.example.eraofband.remote.user.userFollowList.UserFollowListService
import com.example.eraofband.remote.user.userFollowList.UserFollowListView
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowService
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowView
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import java.util.*

class FollowerFragment(var userIdx: Int) : Fragment(), UserFollowListView, UserFollowView,
    UserUnfollowView, RefreshJwtView {
    private var _binding: FragmentFollowerBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var mAdapter: FollowerRVAdapter
    private lateinit var followers : List<FollowerInfo>
    private var searchLists = ArrayList<FollowerInfo>()

    private val userFollowList = UserFollowListService()
    private val userFollowService = UserFollowService()
    private val userUnfollowService = UserUnfollowService()
    private val refreshJwtService = RefreshJwtService()

    private var api = 0
//
//    FollowFragment(userIdx: Int) {
//
//    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowerBinding.inflate(inflater,container,false)

        binding.followerSearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                val searchId = binding.followerSearchEt.text.toString()
                searchFilter(searchId)
                binding.followerSearchEt.hint = ""
            }
        })


        return binding.root
    }

    override fun onResume() {
        super.onResume() // GET 해당 유저 팔로우리스트
        if (binding.followerSearchEt.hint == "") {
            binding.followerSearchEt.text = null
            binding.followerSearchEt.hint = "팔로워 검색창"
        }

        userFollowList.setUserFollowListView(this)

        userFollowService.setUserFollowView(this)
        userUnfollowService.setUserUnfollowView(this)

        refreshJwtService.setRefreshView(this)

        if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
            api = 1;
            refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, userIdx)
        } else {
            userFollowList.userFollowList(getJwt()!!, userIdx)
        }
    }

    fun searchFilter(searchText: String) {
        searchLists.clear()
        for (i in followers.indices) {
            if (followers[i].nickName.toLowerCase()
                    .contains(searchText.lowercase(Locale.getDefault()))
            ) {
                searchLists.add(followers[i])
            }
        }
        mAdapter.filterList(searchLists)
    }


    private fun connectAdapter(item : List<FollowerInfo>) {
        mAdapter = FollowerRVAdapter()
        binding.followingRv.adapter = mAdapter // 리사이클러뷰 어댑터 연결
        binding.followingRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.followingRv.clearAnimation()
        mAdapter.initFollowList(item) // 팔로우리스트 초기화

        binding.followingRv.apply {
            itemAnimator = null
        }

        mAdapter.setMyItemClickListener(object : FollowerRVAdapter.MyItemClickListener {

            override fun onItemClick(item: FollowerInfo) {
                val intent = Intent(activity, UserMyPageActivity::class.java)
                intent.putExtra("userIdx", item.userIdx)
                startActivity(intent)
            }

            override fun clickMySelf() {
                startActivity(Intent(activity, MyPageActivity::class.java))
            }

            override fun getUserIdx(): Int {
                val userSP =
                    requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
                return userSP.getInt("userIdx", 0)
            }

            override fun follow(userIdx: Int) {
                if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
                    api = 2;
                    refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, userIdx)
                } else {
                    userFollowService.userFollow(getJwt()!!, userIdx)
                }
            }

            override fun unfollow(userIdx: Int) {
                if(System.currentTimeMillis() >= getUser().getLong("expiration", 0)) {
                    api = 3;
                    refreshJwtService.refreshJwt(getUser().getString("refresh", "")!!, userIdx)
                } else {
                    userUnfollowService.userUnfollow(getJwt()!!, userIdx)
                }
            }
        })
    }

    fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // EditText를 제외한 영역을 누르면 키보드를 내려줌
        val focusView = requireActivity().currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val inputMethodManager = requireActivity().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.requireActivity().dispatchTouchEvent(ev)
    }

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUser(): SharedPreferences {
        return requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
    }

    override fun onUserFollowListSuccess(code: Int, result: UserFollowListResult) {
        Log.d("FOLLOWLIST", "$code $result")
        connectAdapter(result.getfollower) // 서버에서 받은 팔로우 리스트 전달
        followers = result.getfollower
    }

    override fun onUserFollowListFailure(code: Int, message: String) {
        Log.d("FOLLOWLIST", "$code $message")
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
        Log.d("USER UNFOLLOW / FAIL", "$code $message")
    }

    // 리프레시 토큰--------------------------------------------
    override fun onPatchSuccess(code: Int, result: RefreshResult) {
        Log.d("REFRESH/SUC", "$result")
        if(api == 1) userFollowList.userFollowList(getJwt()!!, userIdx)
        else if(api == 2) userFollowService.userFollow(getJwt()!!, userIdx)
        else if(api == 3) userUnfollowService.userUnfollow(getJwt()!!, userIdx)
    }

    override fun onPatchFailure(code: Int, message: String) {
        Log.d("REFRESH/FAIL", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}