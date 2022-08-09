package com.example.eraofband.ui.main.chat

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.data.ChatRoom
import com.example.eraofband.databinding.FragmentChatBinding
import com.example.eraofband.remote.chat.getChatList.GetChatListResult
import com.example.eraofband.remote.chat.getChatList.GetChatListService
import com.example.eraofband.remote.chat.getChatList.GetChatListView

class ChatFragment : Fragment(), GetChatListView {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private val chatRVAdapter = ChatRVAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentChatBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        chatRVAdapter.clear()
    }

    override fun onResume() {
        super.onResume()

        val getChatListService = GetChatListService()
        getChatListService.setChatListView(this)
        getChatListService.getChatList(getJwt())
    }

    private fun getJwt() : String {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")!!
    }

    private fun initRVAdapter(chatList : ArrayList<GetChatListResult>){
        binding.chatListRv.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.chatListRv.adapter = chatRVAdapter

        chatRVAdapter.initChatList(chatList)

        chatRVAdapter.setMyItemClickListener(object : ChatRVAdapter.MyItemClickListener{
            override fun onItemClick(chatIdx : Int) {
                activity?.let {
                    val intent = Intent(activity, ChatContentActivity::class.java)
                    startActivity(intent)
                }
            }
        })
    }

    override fun onGetListSuccess(result: ArrayList<GetChatListResult>) {
        Log.d("CHATROOM / SUCCESS", result.toString())
        initRVAdapter(result)
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("CHATROOM / FAIL", "$code $message")
    }
}