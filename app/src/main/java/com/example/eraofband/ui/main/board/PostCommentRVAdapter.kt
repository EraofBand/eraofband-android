package com.example.eraofband.ui.main.board

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemPostCommentBinding
import com.example.eraofband.databinding.ItemPostReplyBinding
import com.example.eraofband.remote.board.getBoard.GetBoardComments

class PostCommentRVAdapter(private val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val COMMENT = 0
    private val REPLY = 1
    private var comment = arrayListOf<GetBoardComments>()

    @SuppressLint("NotifyDataSetChanged")
    fun initComment(comment: List<GetBoardComments>) {
        this.comment.addAll(comment)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addComment(position: Int, comment: GetBoardComments) {
        this.comment.add(position, comment)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteComment(position: Int) {
        this.comment.removeAt(position)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearComment() {
        this.comment.clear()
        notifyDataSetChanged()
    }

    interface MyItemClickListener {
        fun onItemClick(comment: GetBoardComments)
        fun onShowPopUp(commentIdx: Int, position: Int, userIdx: Int, view: View)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            COMMENT -> {
                val binding: ItemPostCommentBinding = ItemPostCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                CommentViewHolder(binding)
            }
            REPLY -> {
                val binding: ItemPostReplyBinding = ItemPostReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                ReplyViewHolder(binding)
            }
            else -> {
                throw RuntimeException("알 수 없는 ViewType입니다.")
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is CommentViewHolder) {
            holder.bind(comment[position])

            // 구분선 제거
            if(position != comment.size - 1) {
                if(comment[position].groupNum == comment[position + 1].groupNum) holder.binding.postCommentLine.visibility = View.GONE
            }

            // 댓글 팝업 메뉴 띄우기
            holder.binding.postCommentMoreIv.setOnClickListener { mItemClickListener.onShowPopUp(comment[position].boardCommentIdx, position, comment[position].userIdx, holder.binding.postCommentMoreIv) }

            // 유저 페이지로 이동
            holder.binding.postCommentProfileIv.setOnClickListener { mItemClickListener.onItemClick(comment[position]) }
            holder.binding.postCommentNicknameTv.setOnClickListener { mItemClickListener.onItemClick(comment[position]) }
        }
        else if(holder is ReplyViewHolder) {
            holder.bind(comment[position])

            // 구분선 제거
            if(position != comment.size - 1) {
                if(comment[position].groupNum == comment[position + 1].groupNum) holder.binding.postReplyLine.visibility = View.GONE
            }

            // 답글 팝업 메뉴 띄우기
            holder.binding.postReplyMoreIv.setOnClickListener { mItemClickListener.onShowPopUp(comment[position].boardCommentIdx, position, comment[position].userIdx, holder.binding.postReplyMoreIv) }

            // 유저 페이지로 이동
            holder.binding.postReplyProfileIv.setOnClickListener { mItemClickListener.onItemClick(comment[position]) }
            holder.binding.postReplyNicknameTv.setOnClickListener { mItemClickListener.onItemClick(comment[position]) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if(comment[position].classNum == 0) COMMENT
               else REPLY
    }

    override fun getItemCount(): Int = comment.size

    inner class CommentViewHolder(val binding: ItemPostCommentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: GetBoardComments) {
            Glide.with(context).load(comment.profileImgUrl)  // 프로필 사진
                .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
                .into(binding.postCommentProfileIv)

            binding.postCommentNicknameTv.text = comment.nickName  // 닉네임
            binding.postCommentCommentTv.text = comment.content  // 댓글 내용
            binding.postCommentTimeTv.text = comment.updatedAt  // 댓글 단 시간
       }
    }

    inner class ReplyViewHolder(val binding: ItemPostReplyBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(comment: GetBoardComments) {
            Glide.with(context).load(comment.profileImgUrl)  // 프로필 사진
                .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
                .into(binding.postReplyProfileIv)

            binding.postReplyNicknameTv.text = comment.nickName  // 닉네임
            binding.postReplyCommentTv.text = comment.content  // 댓글 내용
            binding.postReplyTimeTv.text = comment.updatedAt  // 댓글 단 시간
        }
    }
}