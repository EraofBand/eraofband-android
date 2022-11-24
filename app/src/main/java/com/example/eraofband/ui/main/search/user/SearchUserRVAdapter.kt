package com.example.eraofband.ui.main.search.user

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemSearchUserBinding
import com.example.eraofband.remote.search.getUser.GetSearchUserResult
import com.example.eraofband.remote.user.userFollowList.FollowerInfo

class SearchUserRVAdapter : RecyclerView.Adapter<SearchUserRVAdapter.ViewHolder>() {
    private var userList = arrayListOf<GetSearchUserResult>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun userInfo(userIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initUserList(user : List<GetSearchUserResult>) {
        this.userList.addAll(user)
        notifyDataSetChanged()
        Log.d("HHH", userList.toString())
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<GetSearchUserResult>) {
        userList = filteredList as ArrayList<GetSearchUserResult>
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        userList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchUserRVAdapter.ViewHolder {
        val binding: ItemSearchUserBinding = ItemSearchUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchUserRVAdapter.ViewHolder, position: Int) {
        holder.bind(userList[position])
        holder.binding.searchUserCl.setOnClickListener {
            mItemClickListener.userInfo(userList[position].userIdx)
        }
    }
    override fun getItemCount(): Int = userList.size

    inner class ViewHolder(val binding: ItemSearchUserBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: GetSearchUserResult) {

            Glide.with(itemView).load(list.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.searchUserProfileIv)

            binding.searchUserNicknameTv.text = list.nickName

            binding.searchUserSessionTv.text = when(list.userSession) {  // 세션
                0 -> "보컬"
                1 -> "기타"
                2 -> "베이스"
                3 -> "키보드"
                else -> "드럼"
            }
        }
    }
}