package com.example.eraofband.ui.main.mypage.follow

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemFollowBinding
import com.example.eraofband.remote.user.userFollowList.FollowingInfo
import com.example.eraofband.ui.setOnSingleClickListener

class FollowingRVAdapter : RecyclerView.Adapter<FollowingRVAdapter.ViewHolder>() {
    private var followList = arrayListOf<FollowingInfo>()


    @SuppressLint("NotifyDataSetChanged")
    fun initFollowList(followList : List<FollowingInfo>) {
        this.followList.addAll(followList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<FollowingInfo>) {
        followList = filteredList as ArrayList<FollowingInfo>
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        followList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFollowBinding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followList[position])

        holder.binding.itemFollowNicknameTv.setOnClickListener{  // 팔로우리스트에 있는 유저 클릭 시 이동
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
        holder.binding.itemFollowFollowingButtonIv.setOnSingleClickListener {  // 회색 버튼 눌러서 팔로우 취소
            holder.binding.itemFollowButtonIv.visibility = View.VISIBLE
            holder.binding.itemFollowFollowingButtonIv.visibility = View.INVISIBLE
            mItemClickListener.unfollow(followList[position].userIdx)
        }

        holder.binding.itemFollowButtonIv.setOnSingleClickListener {  // 파란 색버튼 다시 눌러서 팔로우
            holder.binding.itemFollowButtonIv.visibility = View.INVISIBLE
            holder.binding.itemFollowFollowingButtonIv.visibility = View.VISIBLE
            mItemClickListener.follow(followList[position].userIdx)
        }
    }

    override fun getItemCount(): Int = followList.size

    inner class ViewHolder(val binding: ItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: FollowingInfo) {

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
        fun onItemClick(item: FollowingInfo)
        fun clickMySelf()
        fun getUserIdx() : Int
        fun follow(userIdx: Int)
        fun unfollow(userIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

}