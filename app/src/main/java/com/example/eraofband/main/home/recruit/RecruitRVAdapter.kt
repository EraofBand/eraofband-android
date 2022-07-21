package com.example.eraofband.main.home.recruit

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandRecruitBinding

class RecruitRVAdapter : RecyclerView.Adapter<RecruitRVAdapter.ViewHolder>() {
    private var bandList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initBandList(bandList : List<Band>) {
        this.bandList.addAll(bandList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBandRecruitBinding = ItemBandRecruitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bandList[position])

        // 클릭 이벤트
    }
    override fun getItemCount(): Int = bandList.size

    inner class ViewHolder(val binding: ItemBandRecruitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: Band) {
            binding.bandRecruitImgIv.setImageResource(R.drawable.band_profile)
            binding.bandRecruitTitleTv.text = "제목입니다"
            binding.bandRecruitIntroduceTv.text = "소개입니다"
        }
    }
}