package com.example.eraofband.main.home.session.band

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemSessionVolunteerBinding
import com.example.eraofband.main.home.lesson.HomeLessonFragment

class BandRecruitSessionVolunteerRVAdapter : RecyclerView.Adapter<BandRecruitSessionVolunteerRVAdapter.ViewHolder>() {
    private var volunteerList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDecisionPopup(code: String)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initVolunteerList(volunteerList : List<Band>) {
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
        fun bind(volunteer: Band) {
            binding.sessionVolunteerProfileIv.setImageResource(R.drawable.ic_captain)

            binding.sessionVolunteerNicknameTv.text = "닉네임입니다"
            binding.sessionVolunteerIntroTv.text = "소개입니다"
        }
    }
}