package com.example.eraofband.main.home.bandlike

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandListBinding
import com.example.eraofband.databinding.ItemSessionNewBandBinding
import com.example.eraofband.main.home.session.HomeSessionNewBandRVAdapter


class HomeBandLikeRVAdapter(private var likeBandList: ArrayList<Band>) :
    RecyclerView.Adapter<HomeBandLikeRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBandListBinding = ItemBandListBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeBandLikeRVAdapter.ViewHolder, position: Int) {
        holder.bind(likeBandList[position])
    }
    override fun getItemCount(): Int = likeBandList.size

    inner class ViewHolder(val binding: ItemBandListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(band: Band) {
            binding.bandListImgIv.setImageResource(0)
            binding.bandListTitleTv.text = "찜 밴드의 타이틀"
            binding.bandListIntroduceTv.text = "찜 밴드의 한 줄 소개"
            binding.bandListRegionTv.text = "강남구"
            binding.bandListMemberCntTv.text = "4/5"
        }
    }
}