package com.example.eraofband.main.usermypage

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.databinding.ItemPortfolioBinding
import com.example.eraofband.remote.getMyPofol.GetMyPofolResult

class UserMyPagePortfolioRVAdapter : RecyclerView.Adapter<UserMyPagePortfolioRVAdapter.ViewHolder>() {
    private var portfolio = arrayListOf<GetMyPofolResult>()

    @SuppressLint("NotifyDataSetChanged")
    fun initPortfolio(portfolio : List<GetMyPofolResult>) {
        this.portfolio.addAll(portfolio)
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        fun onLookDetail(position : Int)
    }

    private lateinit var mItemClickListener: UserMyPagePortfolioRVAdapter.MyItemClickListener
    fun setMyItemClickListener(itemClickListener: UserMyPagePortfolioRVAdapter.MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserMyPagePortfolioRVAdapter.ViewHolder {
        val binding: ItemPortfolioBinding = ItemPortfolioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(portfolio[position])

        // 클릭 이벤트
        holder.binding.itemPortfolioIv.setOnClickListener { mItemClickListener.onLookDetail(position) }  // 게시물 리스트 화면 띄우기
    }

    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: GetMyPofolResult) {
            Glide.with(itemView).load(portfolio.videoUrl) // 썸네일
                .apply(RequestOptions.centerCropTransform())
                .into(binding.itemPortfolioIv)
        }
    }
}