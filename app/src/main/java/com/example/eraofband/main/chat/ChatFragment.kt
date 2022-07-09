package com.example.eraofband.main.chat

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.data.ChatRoom
import com.example.eraofband.databinding.FragmentChatBinding
import com.example.eraofband.main.MainActivity

class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private var chatRooms = ArrayList<ChatRoom>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.chatListRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        val chatRVAdapter = ChatRVAdapter(chatRooms)
        binding.chatListRv.adapter = chatRVAdapter

        chatRooms.apply {
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
            add(ChatRoom(1, 2, 3, 4, "일해 노예자식"))
        }

        chatRVAdapter.setMyItemClickListener(object : ChatRVAdapter.MyItemClickListener{
            override fun onItemClick() {
                activity?.let {
                    val intent = Intent(activity, ChatContentActivity::class.java)
                    startActivity(intent)
                }
            }
        })


        return binding.root
    }
}