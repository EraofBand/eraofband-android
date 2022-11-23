package com.example.eraofband.ui.main.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.data.ChatComment
import com.example.eraofband.data.ChatRoom
import com.example.eraofband.databinding.ItemChatListBinding
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit

class ChatRVAdapter(private val myUserIdx: Int) : RecyclerView.Adapter<ChatRVAdapter.ViewHolder>() {

    private val chatRoomList = arrayListOf<ChatRoom>()

    // 채팅 내역을 받아오기 위한 파이어베이스
    private val database = Firebase.database
    private val getChatRef = database.getReference("chat")

    private var chatIdx = ""

    private var timestamp : Long = 0


    interface MyItemClickListener{
        fun onItemClick(chatIdx : String, profileImg : String, nickname : String, userIdx : Int, status : Int)
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
        chatIdx = chatRoomList[position].chatRoomIdx
        holder.bind(chatRoomList[position], chatIdx)

    }

    override fun getItemCount(): Int = chatRoomList.size

    inner class ViewHolder(private val binding: ItemChatListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(chatRoom: ChatRoom, chatIdx: String) {
            Glide.with(itemView).load(chatRoom.profileImgUrl)  // 프로필 사진
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform()).into(binding.chatListProfileIv)


            binding.chatListNicknameTv.text = chatRoom.nickname // 닉네임
            binding.chatListTimeTv.text = "1분 전"


            val ref = getChatRef.child(chatIdx).child("comments").orderByKey().limitToLast(1)
            ref.get().addOnSuccessListener {
                for(data in it.children) {
                    val getData = data.getValue(ChatComment::class.java)
                    val nowTime = System.currentTimeMillis()

                    Log.d("GET/DATA", getData!!.message)
                    Log.d("GET/DATA", "${getData.timeStamp}")
                    Log.d("GET/DATA", "${getData!!.readUser}")

                    val timestamp = getData.timeStamp
                    var diff = nowTime - timestamp

                    val second = TimeUnit.MILLISECONDS.toSeconds(diff)
                    val minute = TimeUnit.MILLISECONDS.toMinutes(diff)
                    val hour = TimeUnit.MILLISECONDS.toHours(diff)
                    val day = TimeUnit.MILLISECONDS.toHours(diff)

                    val converted = if (second < 60){
                        "${second}초 전"
                    } else if(minute < 60){
                        "${minute}분 전"
                    } else if(hour < 24) {
                        "${hour}시간 전"
                    } else if(day <= 7) {
                        "${day}일 전"
                    } else {
                        val convertSdf = SimpleDateFormat("yyyy/MM/dd")
                        convertSdf.format(timestamp)
                    }


                    if((getData!!.userIdx != myUserIdx) && !getData.readUser){
                        binding.chatListNotificationIv.visibility = View.VISIBLE }
                    else {
                        binding.chatListNotificationIv.visibility = View.GONE }

                    binding.chatListMessageTv.text = getData!!.message
                    binding.chatListTimeTv.text = converted

                }
            }.addOnFailureListener {
                Log.d("GET/FAIL", "FAIL")
            }

            binding.itemChatListRv.setOnClickListener {
                mItemClickListener.onItemClick(chatRoom.chatRoomIdx, chatRoom.profileImgUrl, chatRoom.nickname, chatRoom.otherUserIdx, chatRoom.status)
            }
        }
    }
}