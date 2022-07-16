package com.example.eraofband.main.mypage.portfolio

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ItemPortfolioListBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

import com.example.eraofband.remote.pofollike.PofolLikeResult
import com.example.eraofband.remote.pofollike.PofolLikeService
import com.example.eraofband.remote.pofollike.PofolLikeView

class PortfolioListRVAdapter(private val portfolio: ArrayList<Portfolio>, private val jwt: String) : RecyclerView.Adapter<PortfolioListRVAdapter.ViewHolder>(), PofolLikeView {

    private var videoPlayer: ExoPlayer? = null
    private val pofolLikeService = PofolLikeService()

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
        pofolLikeService.setLikeView(this)

        // 클릭 이벤트
        // 좋아요 관련
        holder.binding.portfolioListLikeIv.setOnClickListener {
            if(portfolio[position].likeOrNot == "Y") {  // 좋아요가 되어있는 경우, 좋아요 취소 진행
                pofolLikeService.deleteLike(jwt, 2)
                holder.binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_off)
                portfolio[position].likeOrNot = "N"
            }
            else {  // 좋아요가 안되어있는 경우, 좋아요 진행
                pofolLikeService.like(jwt, 2)
                holder.binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_on)
                portfolio[position].likeOrNot = "Y"
            }
        }
        // 댓글 창 관련
        holder.binding.portfolioListComment.setOnClickListener { mItemListener.onShowComment(position) }
    }

    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(portfolio: Portfolio) {
            val mediaItem = MediaItem.fromUri(mItemListener.urlParse(portfolio.vidioUrl))  // 비디오 url
            videoPlayer?.setMediaItem(mediaItem)

            // 포폴 받아오는 게 아직 안되기 때문에 다 임시로 했습니다 나중에 수정 예정
            binding.portfolioListProfileIv.setImageResource(portfolio.imgUrl)
            binding.portfolioListNicknameTv.text = "해리"
            binding.portfolioListDateTv.text = "2022.07.13"
            binding.portfolioListTitleTv.text = portfolio.title
            binding.portfolioListContentTv.text = portfolio.content

            // 비디오 모서리 둥글게
            binding.portfolioListVideo.clipToOutline = true
            binding.portfolioListVideoPv.player = videoPlayer
            videoPlayer?.prepare()

            // 좋아요
            if(portfolio.likeOrNot == "Y") binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_on)
            else binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_off)
        }
    }

    override fun onLikeSuccess(code: Int, result: PofolLikeResult) {
        Log.d("POFOLLIKE", result.toString())
    }

    override fun onLikeFailure(code: Int, message: String) {
        Log.d("POFOLLIKE", message)
    }

    override fun onDeleteLikeSuccess(code: Int, result: String) {
        Log.d("POFOLLIKEDELETE", result)
    }

    override fun onDeleteLikeFailure(code: Int, message: String) {
        Log.d("POFOLLIKEDELETE", message)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        videoPlayer?.release() // 비디오플레이어 해제
    }
}