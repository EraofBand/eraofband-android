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
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.eraofband.databinding.FragmentFollowingBinding
import com.example.eraofband.remote.BasicResponse
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtService
import com.example.eraofband.remote.user.refreshjwt.RefreshJwtView
import com.example.eraofband.remote.user.refreshjwt.RefreshResult
import com.example.eraofband.remote.user.userFollow.UserFollowResponse
import com.example.eraofband.remote.user.userFollow.UserFollowService
import com.example.eraofband.remote.user.userFollow.UserFollowView
import com.example.eraofband.remote.user.userFollowList.FollowingInfo
import com.example.eraofband.remote.user.userFollowList.UserFollowListResult
import com.example.eraofband.remote.user.userFollowList.UserFollowListService
import com.example.eraofband.remote.user.userFollowList.UserFollowListView
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowService
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowView
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import java.util.*

class FollowingFragment(var userIdx: Int) : Fragment(), UserFollowListView, UserFollowView, UserUnfollowView, RefreshJwtView {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var mAdapter: FollowingRVAdapter
    private lateinit var followings : List<FollowingInfo>
    private var searchLists = ArrayList<FollowingInfo>()

    private val userFollowList = UserFollowListService()
    private val userFollowService = UserFollowService()
    private val userUnfollowService = UserUnfollowService()
    private val refreshJwtService = RefreshJwtService()

    private var api = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)

        binding.followingSearchEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                val searchId = binding.followingSearchEt.text.toString()
                searchFilter(searchId)
                binding.followingSearchEt.hint = ""
            }
        })
        return binding.root
    }

    private fun searchFilter(id: String) {
        searchLists.clear()
        for (i in followings.indices) {
            if (followings[i].nickName.toLowerCase()
                    .contains(id.lowercase(Locale.getDefault()))
            ) {
                searchLists.add(followings[i])
            }
        }
        mAdapter.filterList(searchLists)
    }

    override fun onResume() {
        super.onResume()// GET 해당 유저 팔로우리스트

        if (binding.followingSearchEt.hint == "") {
            binding.followingSearchEt.text = null
            binding.followingSearchEt.hint = "팔로잉 검색창"
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

    private fun connectAdapter(item: List<FollowingInfo>) {
        mAdapter = FollowingRVAdapter()
        binding.followingRv.adapter = mAdapter
        binding.followingRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.initFollowList(item)

        val animator: RecyclerView.ItemAnimator? = binding.followingRv.itemAnimator
        if (animator is SimpleItemAnimator) {
            animator.supportsChangeAnimations = false
        }

        mAdapter.setMyItemClickListener(object : FollowingRVAdapter.MyItemClickListener {
            override fun onItemClick(item: FollowingInfo) {
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

    private fun getJwt(): String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUser(): SharedPreferences {
        return requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
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

    override fun onUserFollowListSuccess(code: Int, result: UserFollowListResult) {
        Log.d("FOLLOWLIST", "$code $result")
        connectAdapter(result.getfollowing)
        followings = result.getfollowing
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