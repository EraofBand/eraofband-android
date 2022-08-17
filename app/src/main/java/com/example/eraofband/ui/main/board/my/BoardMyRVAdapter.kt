package com.example.eraofband.ui.main.board.my

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemBoardBinding
import com.example.eraofband.databinding.ItemBoardMyBinding
import com.example.eraofband.remote.board.getBoardList.GetBoardListResult
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListResult

class BoardMyRVAdapter : RecyclerView.Adapter<BoardMyRVAdapter.ViewHolder>() {
    private val boardList = arrayListOf<GetMyBoardListResult>()
    private lateinit var mItemClickListener: MyItemClickListener

    interface MyItemClickListener{ // RV 아이템 클릭 리스너 인터페이스
        fun onItemClick(boardIdx : String)
    }

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){ // 리스너 초기화
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initBoardList(boardList : ArrayList<GetMyBoardListResult>) { // 리스트 초기화
        this.boardList.addAll(boardList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        boardList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBoardMyBinding =
            ItemBoardMyBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(boardList[position])
    }
    override fun getItemCount(): Int = boardList.size

    inner class ViewHolder(private val binding: ItemBoardMyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(boardList: GetMyBoardListResult) {
            var category: String = ""
            category = when(boardList.category) {
                0 -> "자유게시판"
                1 -> "질문게시판"
                2 -> "홍보게시판"
                else -> "거래게시판"
            }
            binding.itemBoardTitleTv.text = boardList.title
            binding.itemBoardInfoTv.text = category + "   |   " + boardList.updatedAt + "   |   조회수 " + boardList.views
            binding.itemBoardLikeTv.text = boardList.boardLikeCount.toString()
            binding.itemBoardCommentTv.text = boardList.commentCount.toString()
        }
    }
}