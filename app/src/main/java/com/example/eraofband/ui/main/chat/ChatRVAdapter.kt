package com.example.eraofband.ui.main.chat

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.data.ChatRoom
import com.example.eraofband.databinding.ItemChatListBinding

class ChatRVAdapter : RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {

    private val chatRoomList = arrayListOf<ChatRoom>()

    interface MyItemClickListener{
        fun onItemClick(chatIdx : String)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initChatList(chatList : List<ChatRoom>) {
        this.chatRoomList.addAll(chatList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        this.chatRoomList.clear()
        notifyDataSetChanged()
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener){
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemChatListBinding =
            ItemChatListBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ChatRVAdapter.ViewHolder, position: Int) {
        holder.bind(chatRoomList[position])

    }

    override fun getItemCount(): Int = chatRoomList.size

    inner class ViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatRoom: ChatRoom) {
            Glide.with(itemView).load(chatRoom.profileImgUrl)  // 프로필 사진
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.chatListProfileIv)

            binding.chatListNicknameTv.text = chatRoom.nickname // 닉네임
            binding.chatListTimeTv.text = "1분 전"
            binding.chatListMessageTv.text = "마지막으로 보낸 메세지"

            binding.itemChatListRv.setOnClickListener {
                mItemClickListener.onItemClick(chatRoom.chatRoomIdx)
            }
        }
    }
}