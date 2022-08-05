package com.example.eraofband.ui.main.search.user

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.data.User
import com.example.eraofband.databinding.ItemSearchUserBinding
import com.example.eraofband.remote.user.userFollowList.FollowerInfo

class SearchUserRVAdapter : RecyclerView.Adapter<SearchUserRVAdapter.ViewHolder>() {
    private var userList = arrayListOf<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserRVAdapter.ViewHolder {
        val binding: ItemSearchUserBinding = ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchUserRVAdapter.ViewHolder, position: Int) {
        //holder.bind(userList[position])
    }
    override fun getItemCount(): Int = userList.size

    inner class ViewHolder(val binding: ItemSearchUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: FollowerInfo) {
            Glide.with(itemView).load(list.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.searchUserProfileIv)

            binding.searchUserNicknameTv.text = list.nickName

        }
    }
}