package com.example.eraofband.ui.main.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.ChatRoom
import com.example.eraofband.databinding.ItemChatListBinding

class ChatRVAdapter(private val chatRoomList : ArrayList<ChatRoom>) : RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {

    interface MyItemClickListener{
        fun onItemClick()
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
            binding.chatListTimeTv.text = chatRoom.recentTime.toString() + "분 전"
            binding.chatListMessageTv.text = chatRoom.lastMessage

            binding.itemChatListRv.setOnClickListener {
                mItemClickListener.onItemClick()
            }

        }
    }
}