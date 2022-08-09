package com.example.eraofband.ui.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.data.Chat
import com.example.eraofband.data.Chat.Companion.VIEW_TYPE_LEFT
import com.example.eraofband.data.Chat.Companion.VIEW_TYPE_RIGHT
import com.example.eraofband.databinding.ItemChatLeftBinding
import com.example.eraofband.databinding.ItemChatRightBinding

class ChatContentRVAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatContents = arrayListOf<Chat>()

    //채팅 왼쪽 뷰홀더
    inner class LeftViewHolder(private val binding : ItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Chat){
            binding.apply {
                //binding.leftChatContentTv.text = item.comments[i].message // 보내는 사람

                Glide.with(itemView) //보내는 사람의 프사
                    .load(item.profileImgUrl)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.leftChatProfileIv)

                binding.leftChatNameTv.text = item.nickName
                //binding.leftChatTimeTv.text = item.comments[i].timeStamp
            }
        }
    }

    // 오른쪽 뷰홀더
    inner class RightViewHolder(private val binding : ItemChatRightBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : Chat){
            binding.apply {
                //binding.rightChatContentTv.text = item.comments[i].message // 보내는 사람
                //binding.rightChatTimeTv.text = item.comments[i].timeStamp
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return chatContents[position].viewType
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            VIEW_TYPE_LEFT ->{
                val binding = ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                LeftViewHolder(binding)
            }
            VIEW_TYPE_RIGHT ->{
                val binding = ItemChatRightBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                RightViewHolder(binding)
            }
            else -> throw RuntimeException("Error")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = chatContents[position]

        when(currentItem.viewType){
            VIEW_TYPE_LEFT -> (holder as LeftViewHolder).bind(currentItem)
            VIEW_TYPE_RIGHT -> (holder as RightViewHolder).bind(currentItem)
        }
    }

    override fun getItemCount(): Int = chatContents.size

}