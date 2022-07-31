package com.example.eraofband.ui.main.home.session.band

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemSessionVolunteerBinding
import com.example.eraofband.remote.band.getBand.Applicants

class BandRecruitSessionVolunteerRVAdapter(private val context: Context, private val bandIdx: Int) : RecyclerView.Adapter<BandRecruitSessionVolunteerRVAdapter.ViewHolder>() {
    private var volunteerList = arrayListOf<Applicants>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDecisionPopup(bandIdx: Int, applicant: Applicants, position: Int)
        fun onShowUserPage(userIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initVolunteerList(volunteerList: List<Applicants>) {
        this.volunteerList.addAll(volunteerList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteVolunteer(position: Int) {
        this.volunteerList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSessionVolunteerBinding = ItemSessionVolunteerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(volunteerList[position])

        // 클릭 이벤트
        holder.binding.sessionVolunteerDecisionTv.setOnClickListener {  // 지원 거절, 수락
            mItemClickListener.onShowDecisionPopup(bandIdx, volunteerList[position], position)
        }

        // 정보 페이지로 이동
        holder.binding.sessionVolunteerLayout.setOnClickListener { mItemClickListener.onShowUserPage(volunteerList[position].userIdx) }
    }
    override fun getItemCount(): Int = volunteerList.size

    inner class ViewHolder(val binding: ItemSessionVolunteerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(volunteer: Applicants) {
            Glide.with(context).load(volunteer.profileImgUrl)  // 프로필 사진 연동
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.sessionVolunteerProfileIv)

            binding.sessionVolunteerSessionTv.text = setSession(volunteer.buSession)  // 세션
            binding.sessionVolunteerNameTv.text = volunteer.nickName  // 닉네임 연동
            binding.sessionVolunteerIntroTv.text = volunteer.introduction  // 소개 연동
            binding.sessionVolunteerTimeTv.text = volunteer.updatedAt  // 지원 시간 연동
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