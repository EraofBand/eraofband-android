package com.example.eraofband.ui.main.home.session

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemPopularBandBinding
import com.example.eraofband.remote.band.getPopularBand.GetPopularBandResult


class HomeSessionPopularBandRVAdapter(private val context: Context) : RecyclerView.Adapter<HomeSessionPopularBandRVAdapter.ViewHolder>() {
    private var popularBandList = arrayListOf<GetPopularBandResult>()

    @SuppressLint("NotifyDataSetChanged")
    fun initPopularBand(popularBandList: List<GetPopularBandResult>) {
        this.popularBandList.addAll(popularBandList)
        Log.d("POPULAR", popularBandList.toString())
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
        val binding: ItemPopularBandBinding = ItemPopularBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(popularBandList[position])
        holder.binding.popularBandOrderTv.text = "${position + 1}"  // 순서 연동

        // 밴드 모집 페이지로 이동
        holder.binding.popularBandLayout.setOnClickListener { mItemClickListener.onShowBandInfo(popularBandList[position].bandIdx) }
    }
    override fun getItemCount(): Int = popularBandList.size

    inner class ViewHolder(val binding: ItemPopularBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: GetPopularBandResult) {
            Glide.with(context).load(band.bandImgUrl)  // 프로필 사진 연동
                .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
                .into(binding.popularBandProfileIv)

            binding.popularBandTitleTv.text = band.bandTitle  // 밴드 이름 연동
            binding.popularBandIntroductionTv.text = band.bandIntroduction  // 밴드 소개 연동
        }
    }
}