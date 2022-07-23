package com.example.eraofband.main.home.bandlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandListBinding
import com.example.eraofband.databinding.ItemBandMemberBinding

class BandMemberRVAdapter : RecyclerView.Adapter<BandMemberRVAdapter.ViewHolder>() {
    private var memberList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initMemberList(memberList : List<Band>) {
        this.memberList.addAll(memberList)
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
        fun bind(member: Band) {
            binding.bandMemberProfileIv.setImageResource(R.drawable.ic_captain)

            binding.bandMemberNicknameTv.text = "닉네임입니다"
            binding.bandMemberIntroTv.text = "소개입니다"
        }
    }
}