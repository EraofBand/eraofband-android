package com.example.eraofband.ui.main.search.band

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemSearchBandBinding
import com.example.eraofband.remote.search.getBand.GetSearchBandResult

class SearchBandRVAdapter : RecyclerView.Adapter<SearchBandRVAdapter.ViewHolder>() {
    private var bandList = arrayListOf<GetSearchBandResult>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun bandInfo(bandIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initBandList(band : List<GetSearchBandResult>) {
        this.bandList.addAll(band)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun filterList(filteredList: List<GetSearchBandResult>) {
        bandList = filteredList as ArrayList<GetSearchBandResult>
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        bandList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchBandRVAdapter.ViewHolder {
        val binding: ItemSearchBandBinding = ItemSearchBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchBandRVAdapter.ViewHolder, position: Int) {
        holder.bind(bandList[position])

        holder.binding.searchBandCl.setOnClickListener {
            mItemClickListener.bandInfo(bandList[position].bandIdx)
        }
    }
    override fun getItemCount(): Int = bandList.size

    inner class ViewHolder(val binding: ItemSearchBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(list: GetSearchBandResult) {
            Glide.with(itemView).load(list.bandImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.searchBandImgIv)
            binding.searchBandImgIv.clipToOutline = true

            binding.searchBandNameTv.text = list.bandTitle
            binding.searchBandRegionTv.text = list.bandRegion
            binding.searchBandMemberCntTv.text = "${list.memberCount} / ${list.capacity}"


        }
    }
}