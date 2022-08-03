package com.example.eraofband.ui.main.mypage.follow

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemFollowBinding
import com.example.eraofband.remote.user.userFollow.UserFollowResponse
import com.example.eraofband.remote.user.userFollow.UserFollowService
import com.example.eraofband.remote.user.userFollow.UserFollowView
import com.example.eraofband.remote.user.userFollowList.FollowerInfo
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowResponse
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowService
import com.example.eraofband.remote.user.userUnfollow.UserUnfollowView
import com.example.eraofband.ui.main.mypage.MyPageActivity

class FollowerRVAdapter() : RecyclerView.Adapter<FollowerRVAdapter.ViewHolder>(), UserFollowView, UserUnfollowView {
    private var followList = arrayListOf<FollowerInfo>()
    private var userFollowService = UserFollowService()
    private var userUnfollowService = UserUnfollowService()

    @SuppressLint("NotifyDataSetChanged")
    fun initFollowList(followList : List<FollowerInfo>) {
        this.followList.addAll(followList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFollowBinding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followList[position])
        userFollowService.setUserFollowView(this)
        userUnfollowService.setUserUnfollowView(this)

        holder.binding.itemFollowNicknameTv.setOnClickListener{  // 팔로우리스트에 있는 유저 클릭 시 페이지 이동
            if (followList[position].userIdx == mItemClickListener.getUserIdx()) {
                mItemClickListener.clickMySelf()
            } else {
                mItemClickListener.onItemClick(followList[position])
            }
        }
        holder.binding.itemFollowProfileIv.setOnClickListener {
            if (followList[position].userIdx == mItemClickListener.getUserIdx()) {
                mItemClickListener.clickMySelf()
            } else {
                mItemClickListener.onItemClick(followList[position])
            }
        }
        if (followList[position].userIdx == mItemClickListener.getUserIdx()) { // 팔로우 리스트에 나 자신은 버튼 숨기기
            holder.binding.itemFollowButtonIv.visibility = View.INVISIBLE
            holder.binding.itemFollowFollowingButtonIv.visibility = View.INVISIBLE
        }
        holder.binding.itemFollowFollowingButtonIv.setOnClickListener {  // 회색 버튼 눌러서 팔로우 취소
            holder.binding.itemFollowButtonIv.visibility = View.VISIBLE
            holder.binding.itemFollowFollowingButtonIv.visibility = View.INVISIBLE
            userUnfollowService.userUnfollow(mItemClickListener.getJwt()!!, followList[position].userIdx)
        }

        holder.binding.itemFollowButtonIv.setOnClickListener {  // 파란 색버튼 다시 눌러서 팔로우
            holder.binding.itemFollowButtonIv.visibility = View.INVISIBLE
            holder.binding.itemFollowFollowingButtonIv.visibility = View.VISIBLE
            userFollowService.userFollow(mItemClickListener.getJwt()!!, followList[position].userIdx)
        }
    }
    override fun getItemCount(): Int = followList.size

    inner class ViewHolder(val binding: ItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: FollowerInfo) {
            Glide.with(itemView).load(list.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.itemFollowProfileIv)

            binding.itemFollowNicknameTv.text = list.nickName

             if (list.follow == 0) {
                binding.itemFollowButtonIv.visibility = View.VISIBLE
                binding.itemFollowFollowingButtonIv.visibility = View.INVISIBLE
            } else {
                 binding.itemFollowButtonIv.visibility = View.INVISIBLE
                 binding.itemFollowFollowingButtonIv.visibility = View.VISIBLE
            }
        }
    }
    interface MyItemClickListener {
        fun onItemClick(item: FollowerInfo)
        fun clickMySelf()
        fun getJwt() : String?
        fun getUserIdx() : Int
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    // 팔로우 리스폰스----------------------------------------------------------------
    override fun onUserFollowSuccess(code: Int, response: UserFollowResponse) {
        Log.d("USER FOLLOW / SUCCESS", "코드 : $code / 응답 : $response")
    }

    override fun onUserFollowFailure(code: Int, message: String) {
        Log.d("USER FOLLOW / FAIL", "$code $message")
    }

    // 언팔로우 리스폰스-------------------------------------------------------------------
    override fun onUserUnfollowSuccess(code: Int, response: UserUnfollowResponse) {
        Log.d("USER UNFOLLOW / SUCCESS", "코드 : $code / 응답 : $response")
    }

    override fun onUserUnfollowFailure(code: Int, message: String) {
        Log.d("USER UNFOLLOW / FAIL", "$code $message")
    }
}