package com.example.eraofband.ui.main.usermypage

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.databinding.ItemPortfolioListBinding
import com.example.eraofband.remote.portfolio.getMyPofol.GetMyPofolResult
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeResult
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeService
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeView
import com.example.eraofband.ui.main.mypage.portfolio.PortfolioListRVAdapter
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

class UserPortfolioListRVAdapter(private val jwt : String, private val context: Context) : RecyclerView.Adapter<UserPortfolioListRVAdapter.ViewHolder>(),
    PofolLikeView {
    private val portfolio = arrayListOf<GetMyPofolResult>()
    private var videoPlayer: ExoPlayer? = null
    private var mediaItem: MediaItem? = null
    private val pofolLikeService = PofolLikeService()
    private lateinit var mItemListener: MyItemListener

    @SuppressLint("NotifyDataSetChanged")
    fun initPortfolio(portfolio : List<GetMyPofolResult>) {
        this.portfolio.addAll(portfolio)
        notifyDataSetChanged()
    }

    interface MyItemListener {
        fun urlParse(url : String) : Uri
        fun onShowComment(pofolIdx : Int)
        fun onShowPopup(portfolio: GetMyPofolResult, position: Int, view: View)
        fun onShowInfoPage(userIdx: Int)
    }

    fun setMyItemClickListener(itemListener: MyItemListener) {
        mItemListener = itemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserPortfolioListRVAdapter.ViewHolder {
        val binding: ItemPortfolioListBinding = ItemPortfolioListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        videoPlayer = ExoPlayer.Builder(parent.context).build()

        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserPortfolioListRVAdapter.ViewHolder, position: Int) {
        holder.bind(portfolio[position])
        pofolLikeService.setLikeView(this)
        videoPlayer = ExoPlayer.Builder(context).build() // 비디오플레이어 초기화

        // 좋아요 관련
        holder.binding.portfolioListLikeIv.setOnClickListener {
            if(portfolio[position].likeOrNot == "Y") {  // 좋아요가 되어있는 경우, 좋아요 취소 진행
                pofolLikeService.deleteLike(jwt, portfolio[position].pofolIdx)
                holder.binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_off)

                portfolio[position].pofolLikeCount -= 1
                holder.binding.portfolioListLikeCntTv.text = portfolio[position].pofolLikeCount.toString()
                portfolio[position].likeOrNot = "N"
            }
            else {  // 좋아요가 안되어있는 경우, 좋아요 진행
                pofolLikeService.like(jwt, portfolio[position].pofolIdx)
                holder.binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_on)

                portfolio[position].pofolLikeCount += 1
                holder.binding.portfolioListLikeCntTv.text = portfolio[position].pofolLikeCount.toString()
                portfolio[position].likeOrNot = "Y"
            }
        }

        // 프사 누르면 유저 페이지로 전환
        holder.binding.portfolioListProfileIv.setOnClickListener {
            mItemListener.onShowInfoPage(portfolio[position].userIdx)
            holder.binding.portfolioListVideoPv.player?.stop()
        }
        holder.binding.portfolioListNicknameTv.setOnClickListener {
            mItemListener.onShowInfoPage(portfolio[position].userIdx)
            holder.binding.portfolioListVideoPv.player?.stop()
        }

        // 댓글 창 관련
        holder.binding.portfolioListComment.setOnClickListener {
            mItemListener.onShowComment(portfolio[position].pofolIdx)
            holder.binding.portfolioListVideoPv.player?.stop()
        }

        // 댓글 수정, 신고하기 popup menu 띄우기
        holder.binding.portfolioListListIv.setOnClickListener {
            mItemListener.onShowPopup(portfolio[position], position, holder.binding.portfolioListListIv)
            holder.binding.portfolioListVideoPv.player?.stop()
        }
    }
    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(portfolio: GetMyPofolResult) {
            mediaItem = MediaItem.fromUri(mItemListener.urlParse(portfolio.videoUrl))  // 비디오 url
            videoPlayer?.setMediaItem(mediaItem!!)

            // 내 정보
            Glide.with(context).load(portfolio.profileImgUrl)  // 프로필 사진
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.portfolioListProfileIv)
            binding.portfolioListNicknameTv.text = portfolio.nickName  // 닉네임

            // 비디오플레이어 관련
            binding.portfolioListVideo.clipToOutline = true             // 비디오 모서리 둥글게
            binding.portfolioListVideoPv.player = videoPlayer
            videoPlayer?.prepare()

            binding.portfolioListDateTv.text = portfolio.updatedAt
            binding.portfolioListTitleTv.text = portfolio.title
            binding.portfolioListContentTv.text = portfolio.content

            // 좋아요
            if(portfolio.likeOrNot == "Y") binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_on)
            else binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_off)
            binding.portfolioListLikeCntTv.text = portfolio.pofolLikeCount.toString()
            binding.portfolioListCommentCntTv.text = portfolio.commentCount.toString()
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

    override fun onViewRecycled(holder: ViewHolder) {
        holder.binding.portfolioListVideoPv.player?.release()
        holder.binding.portfolioListVideoPv.player = null
        super.onViewRecycled(holder)
    }
}