package com.example.eraofband.main.home.bandlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandMemberBinding
import com.example.eraofband.databinding.ItemSessionVolunteerBinding

class SessionVolunteerRVAdapter : RecyclerView.Adapter<SessionVolunteerRVAdapter.ViewHolder>() {
    private var volunteerList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
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