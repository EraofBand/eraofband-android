package com.example.eraofband.main.home.session.band

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandMemberBinding
import com.example.eraofband.remote.getBand.SessionMembers

class BandMemberRVAdapter(private val context: Context) : RecyclerView.Adapter<BandMemberRVAdapter.ViewHolder>() {
    private var memberList = arrayListOf<SessionMembers>()

    interface MyItemClickListener {
        // 클릭 이벤트
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initMemberList(memberList : List<SessionMembers>) {
        this.memberList.addAll(memberList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addMember(memberList : SessionMembers) {
        this.memberList.add(memberList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBandMemberBinding = ItemBandMemberBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(memberList[position])

        // 클릭 이벤트
    }
    override fun getItemCount(): Int = memberList.size

    inner class ViewHolder(val binding: ItemBandMemberBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(member: SessionMembers) {
            // 글라이드를 이용한 프로필사진 연동
            Glide.with(context).load(member.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.bandMemberProfileIv)

            binding.bandMemberNicknameTv.text = member.nickName  // 닉네임 연동
            binding.bandMemberIntroTv.text = member.introduction  // 소개 연동
            binding.bandMemberSessionTv.text = setSession(member.buSession)  // 세션 연동
        }
    }

    private fun setSession(session : Int): String {
        // 나중에 이미지 확정되면 이미지도 넣을 예정
        return when (session) {
            0 -> "보컬"
            1 -> "기타"
            2 -> "베이스"
            3 -> "키보드"
            else -> "드럼"
        }
    }
}