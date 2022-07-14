package com.example.eraofband.main.mypage.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ItemPortfolioBinding

class MyPagePortfolioRVAdapter(private var pofolItemList: ArrayList<Portfolio>) : RecyclerView.Adapter<MyPagePortfolioRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        // 자기가 누른 포트폴리오 position을 넘겨주면서 activity 전환
        fun onLookDetail(position : Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPortfolioBinding = ItemPortfolioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pofolItemList[position])

        // 클릭 이벤트
        holder.binding.itemPortfolioIv.setOnClickListener { mItemClickListener.onLookDetail(position) }  // 게시물 리스트 화면 띄우기
    }

    override fun getItemCount(): Int = pofolItemList.size

    inner class ViewHolder(val binding: ItemPortfolioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            binding.itemPortfolioIv.setImageResource(portfolio.imgUrl)
        }
    }
}