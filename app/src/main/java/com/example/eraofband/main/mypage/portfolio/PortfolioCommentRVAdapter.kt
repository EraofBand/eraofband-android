package com.example.eraofband.main.mypage.portfolio

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.databinding.ItemCommentBinding
import com.example.eraofband.remote.portfolio.PofolCommentResult

class PortfolioCommentRVAdapter : RecyclerView.Adapter<PortfolioCommentRVAdapter.ViewHolder>() {
    private var comment = arrayListOf<PofolCommentResult>()

    @SuppressLint("NotifyDataSetChanged")
    fun initComment(comment: List<PofolCommentResult>) {
        this.comment.addAll(comment)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addComment(comment: PofolCommentResult) {
        this.comment.add(comment)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteComment(position: Int) {
        this.comment.removeAt(position)
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        fun onShowPopUp(commentIdx: Int,position: Int, userIdx: Int, view: View)
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

        // 댓글 팝업 메뉴 띄우기
        holder.binding.commentMoreIv.setOnClickListener { mItemClickListener.onShowPopUp(comment[position].pofolCommentIdx, position, comment[position].userIdx, holder.binding.commentMoreIv) }
    }

    override fun getItemCount(): Int = comment.size

    inner class ViewHolder(val binding: ItemCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: PofolCommentResult) {
            binding.commentProfileIv.setImageResource(R.drawable.ic_captain)  // 나중에 img 넣을 예정
            binding.commentNicknameTv.text = comment.nickName
            binding.commentCommentTv.text = comment.content
            binding.commentTimeTv.text = comment.updatedAt
       }
    }
}