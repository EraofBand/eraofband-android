package com.example.eraofband.main.mypage.follow

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemFollowBinding
import com.example.eraofband.remote.userfollow.UserFollowResponse
import com.example.eraofband.remote.userfollow.UserFollowView
import com.example.eraofband.remote.userfollowlist.FollowerInfo
import com.example.eraofband.remote.userunfollow.UserUnfollowResponse
import com.example.eraofband.remote.userunfollow.UserUnfollowView

class FollowerRVAdapter() : RecyclerView.Adapter<FollowerRVAdapter.ViewHolder>(), UserFollowView, UserUnfollowView {
    private var followList = arrayListOf<FollowerInfo>()

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

        holder.binding.itemFollowFollowingButtonIv.setOnClickListener {
            holder.binding.itemFollowButtonIv.visibility = View.VISIBLE
            holder.binding.itemFollowFollowingButtonIv.visibility = View.INVISIBLE
        }

        holder.binding.itemFollowButtonIv.setOnClickListener {
            holder.binding.itemFollowButtonIv.visibility = View.INVISIBLE
            holder.binding.itemFollowFollowingButtonIv.visibility = View.VISIBLE
        }

        holder.binding.itemFollowNicknameTv.setOnClickListener{  // 팔로우리스트에 있는 유저 클릭 시 이동
            mItemClickListener.onItemClick(followList[position])
        }
    }
    override fun getItemCount(): Int = followList.size

    inner class ViewHolder(val binding: ItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: FollowerInfo) {

            Glide.with(itemView).load(list.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.itemFollowProfileIv)

            binding.itemFollowNicknameTv.text = list.nickName
        }
    }
    interface MyItemClickListener {
        fun onItemClick(item: FollowerInfo)
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
        Log.d("USER UNLLOW / FAIL", "$code $message")
    }
}