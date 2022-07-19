package com.example.eraofband.main.mypage.portfolio

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Comment
import com.example.eraofband.data.Portfolio
import com.example.eraofband.databinding.ItemCommentBinding
import com.example.eraofband.databinding.ItemPortfolioListBinding

class PortfolioCommentRVAdapter(private val comment : ArrayList<Comment>) : RecyclerView.Adapter<PortfolioCommentRVAdapter.ViewHolder>() {
    interface MyItemClickListener {
        fun onItemClick()
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemCommentBinding = ItemCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(comment[position])
    }

    override fun getItemCount(): Int = comment.size

    inner class ViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: Comment) {
            // 여기도 포폴이랑 마찬가지로 데이터 받으면 바뀔 예정입니다, 데이터 클래스도 마찬가지
            binding.commentProfileIv.setImageResource(R.drawable.ic_captain)
            binding.commentNicknameTv.text = "해리"
            binding.commentCommentTv.text = comment.comment
            binding.commentTimeTv.text = "1시간 전"

            binding.commentProfileIv.setOnClickListener {
                mItemClickListener.onItemClick()
            }
       }
    }
}