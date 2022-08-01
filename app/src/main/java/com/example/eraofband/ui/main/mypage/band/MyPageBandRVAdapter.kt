package com.example.eraofband.ui.main.mypage.band

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.eraofband.databinding.ItemMypageBandBinding
import com.example.eraofband.remote.user.getMyPage.GetUserBand
import com.example.eraofband.remote.user.getMyPage.GetUserLesson

class MyPageBandRVAdapter(private val context: Context) : RecyclerView.Adapter<MyPageBandRVAdapter.ViewHolder>() {
    private var bandList = arrayListOf<GetUserBand>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDetail(bandIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initBandList(bandList : List<GetUserBand>) {
        this.bandList.addAll(bandList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemMypageBandBinding = ItemMypageBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(bandList[position])

        // 클릭 이벤트
        holder.binding.bandListLayout.setOnClickListener { mItemClickListener.onShowDetail(bandList[position].bandIdx) }  // 나중에는 밴드 아이디를 넣어서 정보 연동
    }
    override fun getItemCount(): Int = bandList.size

    inner class ViewHolder(val binding: ItemMypageBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: GetUserBand) {
            Glide.with(context).load(band.bandImgUrl).into(binding.bandListImgIv)
            binding.bandListImgIv.clipToOutline = true  // 모서리 깎기

            binding.bandListRegionTv.text = band.bandRegion
            binding.bandListTitleTv.text = band.bandTitle
            binding.bandListIntroduceTv.text = band.bandIntroduction
        }
    }
}