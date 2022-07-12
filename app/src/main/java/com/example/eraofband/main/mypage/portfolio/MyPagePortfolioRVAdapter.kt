package com.example.eraofband.main.mypage.portfolio

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ItemPortfolioBinding

class MyPagePortfolioRVAdapter(private var pofolItemList: ArrayList<Portfolio>) : RecyclerView.Adapter<MyPagePortfolioRVAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPortfolioBinding = ItemPortfolioBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pofolItemList[position])
    }

    override fun getItemCount(): Int = pofolItemList.size

    inner class ViewHolder(private val binding: ItemPortfolioBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(portfolio: Portfolio) {
            binding.itemPortfolioIv.setImageResource(portfolio.img!!)
        }
    }
}