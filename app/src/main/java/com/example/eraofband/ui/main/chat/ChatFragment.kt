package com.example.eraofband.ui.main.chat

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
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

    private lateinit var chatRVAdapter: ChatRVAdapter
    private var chatRooms = ArrayList<ChatRoom>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentChatBinding.inflate(inflater, container, false)

        binding.root.setOnClickListener {
            if(binding.chatSearchEt.isFocused) hideKeyboard()
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val getChatListService = GetChatListService()
        getChatListService.setChatListView(this)
        getChatListService.getChatList(getJwt()!!)

    }


    private fun initRVAdapter(result: ArrayList<ChatRoom>) {
        chatRVAdapter = ChatRVAdapter()
        binding.chatListRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.chatListRv.adapter = chatRVAdapter

        chatRVAdapter.clear()

        chatRVAdapter.initChatList(result)

        chatRVAdapter.setMyItemClickListener(object : ChatRVAdapter.MyItemClickListener{
            override fun onItemClick(chatIdx : String, profileImg: String, nickname : String) {
                activity?.let {
                    val intent = Intent(activity, ChatContentActivity::class.java)
                    intent.putExtra("chatRoomIndex", chatIdx)
                    intent.putExtra("profile", profileImg)
                    intent.putExtra("nickname", nickname)
                    startActivity(intent)
                }
            }
        })
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // EditText를 제외한 영역을 누르면 키보드를 내려줌
        val focusView = activity?.currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val inputMethodManager = activity?.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.requireActivity().dispatchTouchEvent(ev)
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetListSuccess(result: ArrayList<GetChatListResult>) {
        Log.d("GET CHAT / SUCCESS", result.toString())

        // 결과값에서 채팅룸 인덱스, 닉네임, 프로필사진만 먼저 가져옴
        for (i in 0 until result.size)
            chatRooms.add(i, ChatRoom(result[i].chatRoomIdx, result[i].nickName, result[i].profileImgUrl,
                "", "", true))


        initRVAdapter(chatRooms)
        chatRooms.clear()
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("GET CHAT / FAIL", "$code $message")
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}