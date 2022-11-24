package com.example.eraofband.ui.main.home.session.band

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemBandListBinding
import com.example.eraofband.remote.band.getRegionBand.GetRegionBandResult

class BandListRVAdapter : RecyclerView.Adapter<BandListRVAdapter.ViewHolder>() {
    private var bandList = arrayListOf<GetRegionBandResult>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDetail(bandIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initBandList(bandList: List<GetRegionBandResult>) {
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
        holder.binding.bandListLayout.setOnClickListener { mItemClickListener.onShowDetail(bandList[position].bandIdx) }  // 나중에는 밴드 아이디를 넣어서 정보 연동
    }
    override fun getItemCount(): Int = bandList.size

    inner class ViewHolder(val binding: ItemBandListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: GetRegionBandResult) {
            Glide.with(itemView).load(band.bandImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.bandListImgIv)
            binding.bandListImgIv.clipToOutline = true  // 모서리 깎기

            binding.bandListTitleTv.text = band.bandTitle
            binding.bandListIntroduceTv.text = band.bandIntroduction
            binding.bandListRegionTv.text = band.bandRegion
            binding.bandListMemberCntTv.text = "${band.memberCount} / ${band.capacity}"
        }
    }
}