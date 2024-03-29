package com.example.eraofband.ui.main.mypage.portfolio

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
import com.example.eraofband.remote.portfolio.getPofol.GetPofolResult
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeResult
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeService
import com.example.eraofband.remote.portfolio.pofolLike.PofolLikeView
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.kakao.sdk.template.model.*

class PortfolioListRVAdapter(private val jwt: String, private val context: Context) : RecyclerView.Adapter<PortfolioListRVAdapter.ViewHolder>(),
    PofolLikeView {
    private val portfolio = arrayListOf<GetPofolResult>()
    private var videoPlayer: ExoPlayer? = null
    private val pofolLikeService = PofolLikeService()
    private lateinit var mItemListener: MyItemListener
    //private lateinit var defaultFeed: FeedTemplate

    @SuppressLint("NotifyDataSetChanged")
    fun initPortfolio(portfolio : List<GetPofolResult>) {
        this.portfolio.addAll(portfolio)
        notifyDataSetChanged()
    }

    // 나중에 포트폴리오 추가, 삭제를 위해서 이렇게 함수로 추가, 삭제하도록 만들었습니다 변경 값이 바로바로 화면에 나타나야하니까!
    @SuppressLint("NotifyDataSetChanged")
    fun addPortfolio(portfolio: GetPofolResult) {
        this.portfolio.add(portfolio)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePortfolio(position: Int) {
        this.portfolio.removeAt(position)
        notifyDataSetChanged()
    }

    interface MyItemListener {
        fun urlParse(url : String) : Uri
        fun onShowComment(pofolIdx : Int)
        fun onShowPopup(portfolio: GetPofolResult, position: Int, view: View)
        fun onShowInfoPage(userIdx: Int)
        fun sharePofol(defaultFeed: FeedTemplate)
    }

    fun setMyItemClickListener(itemListener: MyItemListener) {
        mItemListener = itemListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPortfolioListBinding = ItemPortfolioListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        videoPlayer = ExoPlayer.Builder(parent.context).build() // 비디오플레이어 초기화
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(portfolio[position])
        pofolLikeService.setLikeView(this)
        videoPlayer = ExoPlayer.Builder(context).build() // 비디오플레이어 초기화

        // 좋아요 관련
        holder.binding.portfolioListLikeIv.setOnClickListener {
            if (portfolio[position].likeOrNot == "Y") {  // 좋아요가 되어있는 경우, 좋아요 취소 진행
                pofolLikeService.deleteLike(jwt, portfolio[position].pofolIdx)
                holder.binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_off)

                portfolio[position].pofolLikeCount -= 1
                holder.binding.portfolioListLikeCntTv.text =
                    portfolio[position].pofolLikeCount.toString()
                portfolio[position].likeOrNot = "N"
            } else {  // 좋아요가 안되어있는 경우, 좋아요 진행
                pofolLikeService.like(jwt, portfolio[position].pofolIdx)
                holder.binding.portfolioListLikeIv.setImageResource(R.drawable.ic_heart_on)

                portfolio[position].pofolLikeCount += 1
                holder.binding.portfolioListLikeCntTv.text =
                    portfolio[position].pofolLikeCount.toString()
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
            mItemListener.onShowPopup(
                portfolio[position],
                position,
                holder.binding.portfolioListListIv
            )
            holder.binding.portfolioListVideoPv.player?.stop()
        }
    }

    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(portfolio: GetPofolResult) {
            val mediaItem = MediaItem.fromUri(mItemListener.urlParse(portfolio.videoUrl))  // 비디오 url
            videoPlayer?.setMediaItem(mediaItem)
            
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

            // 카카오링크 공유
            var defaultFeed = FeedTemplate(
                content = Content(
                    title = portfolio.title,
                    description = portfolio.content,
                    imageUrl = portfolio.imgUrl,
                    link = Link(
                    )
                ),
                social = Social(
                    likeCount = portfolio.pofolLikeCount,
                    commentCount = portfolio.commentCount
                ),
                buttons = listOf(
                    Button(
                        "앱으로 보기",
                        Link(
                            androidExecutionParams = mapOf("test" to "test"),
                            iosExecutionParams = mapOf("test" to "test")
                        )
                    )
                )
            )
            // 포트폴리오 공유하기
            binding.porfolioListShareIv.setOnClickListener {
                mItemListener.sharePofol(defaultFeed)
            }
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
        super.onViewRecycled(holder)
        holder.binding.portfolioListVideoPv.player?.release()
        holder.binding.portfolioListVideoPv.player = null
    }
}