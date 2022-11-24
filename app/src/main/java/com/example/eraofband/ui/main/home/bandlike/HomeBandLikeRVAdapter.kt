package com.example.eraofband.ui.main.home.bandlike

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemBandListBinding
import com.example.eraofband.remote.band.getLikedBand.GetLikedBandResult
import com.example.eraofband.ui.main.home.HomeFabDialog


class HomeBandLikeRVAdapter(): RecyclerView.Adapter<HomeBandLikeRVAdapter.ViewHolder>() {
    private var bandList = arrayListOf<GetLikedBandResult>()

    @SuppressLint("NotifyDataSetChanged")
    fun initLikedBand(band: List<GetLikedBandResult>) {
        this.bandList.addAll(band)
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        fun onShowBandInfo(bandIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBandListBinding = ItemBandListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBandLikeRVAdapter.ViewHolder, position: Int) {
        holder.bind(bandList[position])

        // 클릭 이벤트
        holder.binding.bandListLayout.setOnClickListener { mItemClickListener.onShowBandInfo(bandList[position].bandIdx) }
    }
    override fun getItemCount(): Int = bandList.size

    inner class ViewHolder(val binding: ItemBandListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(band: GetLikedBandResult) {
            Glide.with(itemView).load(band.bandImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.bandListImgIv)  // 밴드 이미지
            binding.bandListImgIv.clipToOutline = true  // 모서리 깎기

            binding.bandListTitleTv.text = band.bandTitle  // 밴드 타이틀
            binding.bandListIntroduceTv.text = band.bandIntroduction  // 밴드 소개
            binding.bandListRegionTv.text = band.bandRegion
            binding.bandListMemberCntTv.text = "${band.memberCount} / ${band.capacity}"  // 밴드 멤버 수
        }
    }
}