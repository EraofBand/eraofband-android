package com.example.eraofband.ui.main.chat

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.data.ChatComment
import com.example.eraofband.databinding.ItemChatLeftBinding
import com.example.eraofband.databinding.ItemChatRightBinding
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat

class ChatContentRVAdapter(private val profileImg : String, private val nickname : String, private val chatIdx : String) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var chatContents = arrayListOf<ChatComment>()

    private var lastTime : Long = 0
    private var viewType = -1
    private var lastIndex = -1

    // 파이어베이스로 값 올리기
    private var mDatabase = FirebaseDatabase.getInstance().reference
    private val sendChatRef = mDatabase.child("chat")

    @SuppressLint("NotifyDataSetChanged")
    fun addNewChat(chatComment: List<ChatComment>){
        this.chatContents.addAll(chatComment)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearChat(){
        this.chatContents.clear()
        notifyDataSetChanged()
    }

    @SuppressLint("SimpleDateFormat")
    fun convertTimestampToDate(timestamp: Long) : String{
        val compareSdf = SimpleDateFormat("yyyy/MM/dd")
        val dateSdf = SimpleDateFormat("MM/dd" +"   " + "hh:mm")
        val timeSdf = SimpleDateFormat("hh:mm")

        return if(compareSdf.format(timestamp) == compareSdf.format(lastTime)){
            val date = timeSdf.format(timestamp)
            date
        } else {
            val date = dateSdf.format(timestamp)
            date
        }
    }

    //채팅 왼쪽 뷰홀더
    inner class LeftViewHolder(private val binding : ItemChatLeftBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ChatComment){
            binding.apply {
                binding.leftChatContentTv.text = item.message // 보내는 사람

                Glide.with(itemView) //보내는 사람의 프사
                    .load(profileImg)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.circleCropTransform())
                    .into(binding.leftChatProfileIv)

                binding.leftChatNameTv.text = nickname
                binding.leftChatTimeTv.text = convertTimestampToDate(item.timeStamp)
            }

            setRead(position)
        }
    }

    private fun setRead(position: Int) {
        val hashMap = HashMap<String, Boolean>()
        hashMap["readUser"] = true

        sendChatRef.child(chatIdx).child("comments").child("$position").updateChildren(hashMap as Map<String, Any>)
    }

    // 오른쪽 뷰홀더
    inner class RightViewHolder(private val binding : ItemChatRightBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item : ChatComment){
            binding.apply {
                binding.rightChatContentTv.text = item.message // 보내는 사람
                binding.rightChatTimeTv.text = convertTimestampToDate(item.timeStamp)

                if(item.readUser){
                    binding.rightChatIndicator.visibility = View.INVISIBLE
                }
            }
        }
    }


    override fun getItemViewType(position: Int): Int {
        return chatContents[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return when(viewType){
            0 ->{
                val binding = ItemChatLeftBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                LeftViewHolder(binding)
            }
            1 ->{
                val binding = ItemChatRightBinding.inflate(LayoutInflater.from(parent.context),parent,false)
                RightViewHolder(binding)
            }
            else -> throw RuntimeException("Error")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        lastTime = if(position > 0){
            chatContents[position - 1].timeStamp
        } else {
            0
        }

        val currentItem = chatContents[position]
        viewType = currentItem.type
        when(currentItem.type){
            0 -> {
                (holder as LeftViewHolder).bind(currentItem)
                 lastIndex = position
                 Log.d("LAST INDEX 2", lastIndex.toString())}
            1 -> (holder as RightViewHolder).bind(currentItem)
        }
    }

    fun returnLastIndex() : Int = lastIndex

    override fun getItemCount(): Int = chatContents.size


}