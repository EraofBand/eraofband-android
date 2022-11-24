package com.example.eraofband.ui.main.board

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.data.PostImgUrl
import com.example.eraofband.databinding.ItemPictureBinding

class BoardImgRVAdapter : RecyclerView.Adapter<BoardImgRVAdapter.ViewHolder>() {

    private var postImgUrl = arrayListOf<PostImgUrl>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onDelete(position: Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initImg(item: List<PostImgUrl>) {
        this.postImgUrl.addAll(item)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun onDelete(position: Int){
        this.postImgUrl.removeAt(position)
        notifyDataSetChanged()
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemPictureBinding = ItemPictureBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(postImgUrl[position])

        holder.binding.itemPictureIv.setOnClickListener {
            mItemClickListener.onDelete(position)
        }
    }

    override fun getItemCount(): Int = postImgUrl.size

    inner class ViewHolder(val binding: ItemPictureBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(postImgUrl: PostImgUrl) {
            Glide.with(itemView).load(postImgUrl.imgUrl)
                .apply(RequestOptions.centerCropTransform())
                .into(binding.itemPictureIv)
            binding.itemPictureIv.clipToOutline = true  // 모서리 깎기
        }
    }
}