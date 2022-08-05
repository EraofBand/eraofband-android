package com.example.eraofband.ui.main.usermypage.band

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemMypageBandBinding
import com.example.eraofband.remote.user.getMyPage.GetUserBand
import com.example.eraofband.remote.user.getMyPage.GetUserLesson

class UserPageBandRVAdapter(private val context: Context) : RecyclerView.Adapter<UserPageBandRVAdapter.ViewHolder>() {
    private var bandList = arrayListOf<com.example.eraofband.remote.user.getOtherUser.GetUserBand>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onShowDetail(bandIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initBandList(bandList : List<com.example.eraofband.remote.user.getOtherUser.GetUserBand>) {
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
        fun bind(band: com.example.eraofband.remote.user.getOtherUser.GetUserBand) {
            Glide.with(context).load(band.bandImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.bandListImgIv)
            binding.bandListImgIv.clipToOutline = true  // 모서리 깎기

            binding.bandListRegionTv.text = band.bandRegion
            binding.bandListTitleTv.text = band.bandTitle
            binding.bandListIntroduceTv.text = band.bandIntroduction
        }
    }
}