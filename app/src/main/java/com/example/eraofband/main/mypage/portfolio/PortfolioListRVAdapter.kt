package com.example.eraofband.main.mypage.portfolio

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ItemPortfolioListBinding

class PortfolioListRVAdapter(private val portfolio : ArrayList<Portfolio>) : RecyclerView.Adapter<PortfolioListRVAdapter.ViewHolder>() {
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(portfolio[position])

        // 클릭 이벤트
        holder.binding.portfolioListComment.setOnClickListener { mItemListener.onShowComment(position) }
    }

    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            // 포폴 받아오는 게 아직 안되기 때문에 다 임시로 했습니다 나중에 수정 예정
            binding.portfolioListProfileIv.setImageResource(portfolio.imgUrl)
            binding.portfolioListNicknameTv.text = "해리"
            binding.portfolioListDateTv.text = "2022.07.13"
            binding.portfolioListTitleTv.text = portfolio.title
            binding.portfolioListContentTv.text = portfolio.content

            binding.portfolioListVideo.clipToOutline = true
            binding.portfolioListVideoVv.setVideoURI(mItemListener.urlParse(portfolio.vidioUrl))
            binding.portfolioListVideoVv.start()
        }
    }

    private fun videoClickListener() {

    }

}