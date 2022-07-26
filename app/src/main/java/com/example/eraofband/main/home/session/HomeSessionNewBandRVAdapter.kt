package com.example.eraofband.main.home.session

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eraofband.databinding.ItemSessionNewBandBinding
import com.example.eraofband.remote.getNewBand.GetNewBandResult


class HomeSessionNewBandRVAdapter(private val context: Context) : RecyclerView.Adapter<HomeSessionNewBandRVAdapter.ViewHolder>() {
    private var newBandList = arrayListOf<GetNewBandResult>()

    @SuppressLint("NotifyDataSetChanged")
    fun initNewBand(band : List<GetNewBandResult>) {
        this.newBandList.addAll(band)
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowBandInfo(bandIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSessionNewBandBinding = ItemSessionNewBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newBandList[position])

        // 밴드 모집 페이지로 이동
    }
    override fun getItemCount(): Int = newBandList.size

    inner class ViewHolder(val binding: ItemSessionNewBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: GetNewBandResult) {
            Glide.with(context).load(band.bandImgUrl).into(binding.itemNewBandProfileIv)  // 밴드 이미지
            binding.itemNewBandProfileIv.clipToOutline = true  // 모서리 깎기

            binding.itemNewBandLocationTv.text = band.bandRegion
            binding.itemNewBandTitleTv.text = band.bandTitle
            binding.itemNewBandMemberCountTv.text = "${band.sessionNum} / ${band.totalNum}"
        }
    }
}