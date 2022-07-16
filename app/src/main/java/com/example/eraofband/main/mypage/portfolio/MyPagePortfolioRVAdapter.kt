package com.example.eraofband.main.mypage.portfolio

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ItemPortfolioBinding
import com.example.eraofband.remote.getMyPofol.GetMyPofolResult

class MyPagePortfolioRVAdapter() : RecyclerView.Adapter<MyPagePortfolioRVAdapter.ViewHolder>() {
    private var portfolio = arrayListOf<GetMyPofolResult>()

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
        holder.bind(portfolio[position])

        // 클릭 이벤트
        holder.binding.itemPortfolioIv.setOnClickListener { mItemClickListener.onLookDetail(position) }  // 게시물 리스트 화면 띄우기
    }

    override fun getItemCount(): Int = portfolio.size

    inner class ViewHolder(val binding: ItemPortfolioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: GetMyPofolResult) {
            // 이미지가 없기 때문에 임의로 넣었습니다요
            binding.itemPortfolioIv.setImageResource(R.drawable.portfolio_spare)
        }
    }
}