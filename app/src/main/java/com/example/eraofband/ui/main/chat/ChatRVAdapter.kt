package com.example.eraofband.ui.main.chat

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.data.ChatRoom
import com.example.eraofband.databinding.ItemChatListBinding
import com.example.eraofband.remote.chat.getChatList.GetChatListResult
import com.example.eraofband.remote.lesson.getLessonInfo.LessonMembers

class ChatRVAdapter() : RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {

    private val chatRoomList = arrayListOf<GetChatListResult>()

    interface MyItemClickListener{
        fun onItemClick(chatIdx : Int)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initChatList(chatList : List<GetChatListResult>) {
        this.chatRoomList.addAll(chatList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clear(){
        chatRoomList.clear()
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

        fun bind(chatRoom: GetChatListResult) {

            Glide.with(itemView)  // 수강생 프사
                .load(chatRoom.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.chatListProfileIv)

            binding.chatListNicknameTv.text = chatRoom.nickName

            binding.itemChatListRv.setOnClickListener {
                mItemClickListener.onItemClick(chatRoom.chatRoomIdx)
            }

        }
    }
}