package com.example.eraofband.main.home.session.band

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.databinding.ItemSessionVolunteerBinding
import com.example.eraofband.remote.getBand.Applicants

class BandRecruitSessionVolunteerRVAdapter(private val context: Context) : RecyclerView.Adapter<BandRecruitSessionVolunteerRVAdapter.ViewHolder>() {
    private var volunteerList = arrayListOf<Applicants>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDecisionPopup(code: String)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initVolunteerList(volunteerList : List<Applicants>) {
        this.volunteerList.addAll(volunteerList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSessionVolunteerBinding = ItemSessionVolunteerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(volunteerList[position])

        // 클릭 이벤트
        holder.binding.sessionVolunteerCheckTv.setOnClickListener { mItemClickListener.onShowDecisionPopup("applicant") }
    }
    override fun getItemCount(): Int = volunteerList.size

    inner class ViewHolder(val binding: ItemSessionVolunteerBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(volunteer: Applicants) {
            binding.sessionVolunteerProfileIv.setImageResource(R.drawable.ic_captain)

            binding.sessionVolunteerSessionTv.text = setSession(volunteer.buSession)

            binding.sessionVolunteerNicknameTv.text = "닉네임입니다"
            binding.sessionVolunteerIntroTv.text = "소개입니다"

            Glide.with(context).load(volunteer.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.sessionVolunteerProfileIv)

            binding.sessionVolunteerNicknameTv.text = volunteer.nickName
            binding.sessionVolunteerIntroTv.text = volunteer.introduction
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