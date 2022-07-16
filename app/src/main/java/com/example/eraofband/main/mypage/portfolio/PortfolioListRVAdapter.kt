package com.example.eraofband.main.mypage.portfolio

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.databinding.ItemPortfolioListBinding
import com.example.eraofband.remote.getMyPofol.GetMyPofolResult
import com.example.eraofband.remote.pofollike.PofolLikeResult
import com.example.eraofband.remote.pofollike.PofolLikeService
import com.example.eraofband.remote.pofollike.PofolLikeView

class PortfolioListRVAdapter(private val jwt: String) : RecyclerView.Adapter<PortfolioListRVAdapter.ViewHolder>(), PofolLikeView {
    private val portfolio = arrayListOf<GetMyPofolResult>()
    private val pofolLikeService = PofolLikeService()

    @SuppressLint("NotifyDataSetChanged")
    fun initPortfolio(portfolio : List<GetMyPofolResult>) {
        this.portfolio.addAll(portfolio)
        notifyDataSetChanged()
    }

    // 나중에 포트폴리오 추가, 삭제를 위해서 이렇게 함수로 추가, 삭제하도록 만들었습니다 변경 값이 바로바로 화면에 나타나야하니까!
    @SuppressLint("NotifyDataSetChanged")
    fun addPortfolio(portfolio: GetMyPofolResult) {
        this.portfolio.add(portfolio)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deletePortfolio(portfolio: GetMyPofolResult) {
        this.portfolio.remove(portfolio)
        notifyDataSetChanged()
    }

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
        return ViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(portfolio[position])
        pofolLikeService.setLikeView(this)

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

        // 댓글 창 관련
        holder.binding.portfolioListComment.setOnClickListener { mItemListener.onShowComment(position) }
    }

    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioListBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(portfolio: GetMyPofolResult) {
            // 내 정보
            binding.portfolioListProfileIv.setImageResource(R.drawable.ic_captain)  // 프사 임의로 지정
            binding.portfolioListNicknameTv.text = portfolio.nickName

            // 비디오 모서리 둥글게
            binding.portfolioListVideo.clipToOutline = true
            binding.portfolioListVideoVv.setVideoURI(mItemListener.urlParse(portfolio.videoUrl))
            binding.portfolioListVideoVv.start()

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

}