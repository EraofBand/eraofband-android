package com.example.eraofband.main.mypage.follow

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemFollowBinding
import com.example.eraofband.remote.userfollowlist.FollowingInfo

class FollowingRVAdapter() : RecyclerView.Adapter<FollowingRVAdapter.ViewHolder>() {
    private var followList = arrayListOf<FollowingInfo>()

    @SuppressLint("NotifyDataSetChanged")
    fun initFollowList(followList : List<FollowingInfo>) {
        this.followList.addAll(followList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemFollowBinding = ItemFollowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(followList[position])
    }

    override fun getItemCount(): Int = followList.size

    inner class ViewHolder(val binding: ItemFollowBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: FollowingInfo) {

            Glide.with(itemView).load(list.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.itemFollowProfileIv)

            binding.itemFollowNicknameTv.text = list.nickName
        }
    }
}