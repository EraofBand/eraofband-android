package com.example.eraofband.ui.main.board.free

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemBoardBinding
import com.example.eraofband.remote.board.getBoardList.GetBoardListResult

class BoardFreeRVAdapter : RecyclerView.Adapter<BoardFreeRVAdapter.ViewHolder>() {
    private val boardList = arrayListOf<GetBoardListResult>()
    private lateinit var mItemClickListener: MyItemClickListener

    interface MyItemClickListener{ // RV 아이템 클릭 리스너 인터페이스
        fun onItemClick(boardIdx : String)
    }

    fun setMyItemClickListener(itemClickListener: MyItemClickListener){ // 리스너 초기화
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initBoardList(boardList : ArrayList<GetBoardListResult>) { // 리스트 초기화
        this.boardList.addAll(boardList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        boardList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBoardBinding =
            ItemBoardBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(boardList[position])
    }
    override fun getItemCount(): Int = boardList.size

    inner class ViewHolder(private val binding: ItemBoardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(boardList: GetBoardListResult) {

            if (boardList.imgUrl == "null") { // 게시물 이미지가 없으면 invisible
                binding.itemBoardImageIv.visibility = View.INVISIBLE
            } else {
                Glide.with(itemView).load(boardList.imgUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .into(binding.itemBoardImageIv)
            }

            binding.itemBoardTitleImageIv.visibility = View.GONE // 타이틀 옆 이미지 없애기
            binding.itemBoardTitleTv.text = boardList.title
            binding.itemBoardIntroTv.text = boardList.content
            binding.itemBoardInfoTv.text = boardList.nickName + "   |   " + boardList.updatedAt + "   |   조회수 " + boardList.views
            binding.itemBoardLikeTv.text = boardList.boardLikeCount.toString()
            binding.itemBoardCommentTv.text = boardList.commentCount.toString()
        }
    }
}