package com.example.eraofband.main.mypage.portfolio

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ItemPortfolioListBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem


class PortfolioListRVAdapter(private val portfolio : ArrayList<Portfolio>) : RecyclerView.Adapter<PortfolioListRVAdapter.ViewHolder>() {

    private var videoPlayer: ExoPlayer? = null

    interface MyItemListener {
        fun urlParse(url : String) : Uri
        fun onShowComment(position : Int)
    }

    private lateinit var mItemListener: MyItemListener
    fun setMyItemClickListener(itemListener: MyItemListener) {
        mItemListener = itemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPortfolioListBinding = ItemPortfolioListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        videoPlayer = ExoPlayer.Builder(parent.context).build()

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(portfolio[position])

        // 클릭 이벤트
        holder.binding.portfolioListComment.setOnClickListener { mItemListener.onShowComment(position) }
    }

    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            val mediaItem = MediaItem.fromUri(mItemListener.urlParse(portfolio.vidioUrl))  // 비디오 url
            videoPlayer?.setMediaItem(mediaItem)

            // 포폴 받아오는 게 아직 안되기 때문에 다 임시로 했습니다 나중에 수정 예정
            binding.portfolioListProfileIv.setImageResource(portfolio.imgUrl)
            binding.portfolioListNicknameTv.text = "해리"
            binding.portfolioListDateTv.text = "2022.07.13"
            binding.portfolioListTitleTv.text = portfolio.title
            binding.portfolioListContentTv.text = portfolio.content

            binding.portfolioListVideo.clipToOutline = true  // 모서리 둥글게
            binding.portfolioListVideoPv.player = videoPlayer
            videoPlayer?.prepare()
        }
    }

    private fun videoClickListener() {

    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        videoPlayer?.release() // 비디오플레이어 해제
    }
}