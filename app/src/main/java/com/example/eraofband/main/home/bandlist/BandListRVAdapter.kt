package com.example.eraofband.main.home.bandlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandListBinding

class BandListRVAdapter : RecyclerView.Adapter<BandListRVAdapter.ViewHolder>() {
    private var bandList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDetail(bandIdx: Int)
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
        val binding: ItemBandListBinding = ItemBandListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bandList[position])

        // 클릭 이벤트
        holder.binding.bandListLayout.setOnClickListener { mItemClickListener.onShowDetail(position) }  // 나중에는 밴드 아이디를 넣어서 정보 연동
    }
    override fun getItemCount(): Int = bandList.size

    inner class ViewHolder(val binding: ItemBandListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: Band) {
            binding.bandListImgIv.setImageResource(R.drawable.band_profile)
            binding.bandListImgIv.clipToOutline = true  // 모서리 깎기

            binding.bandListTitleTv.text = "제목입니다"
            binding.bandListIntroduceTv.text = "소개입니다"
        }
    }
}