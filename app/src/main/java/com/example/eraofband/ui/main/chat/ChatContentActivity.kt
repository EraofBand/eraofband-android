package com.example.eraofband.ui.main.chat

import android.content.Context
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.ChatComment
import com.example.eraofband.data.ChatUser
import com.example.eraofband.data.LastChatIdx
import com.example.eraofband.databinding.ActivityChatContentBinding
import com.example.eraofband.remote.chat.leaveChat.LeaveChatService
import com.example.eraofband.remote.chat.leaveChat.LeaveChatView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatContentActivity : AppCompatActivity(), LeaveChatView {
    private lateinit var binding: ActivityChatContentBinding

    // 채팅 API
    private val leaveChatService = LeaveChatService()

    // 채팅 리사이클러뷰 어뎁터
    private lateinit var chatRVAdapter : ChatContentRVAdapter

    // 유저 정보
    private var firstIndex = -1
    private var secondIndex = -1
    private lateinit var profileImg : String
    private lateinit var nickname : String

    // 인덱스
    private var chatIdx = ""  // 채팅방 인덱스
    private var num = -1  // 불러온 채팅 수
    private var lastChatIdx = -1  // 데베에 저장된 마지막 채팅 인덱스

    // 채팅 내역을 받아오기 위한 파이어베이스
    private val database = Firebase.database
    private val getChatRef = database.getReference("chat")

    // 파이어베이스로 값 올리기
    private var mDatabase = FirebaseDatabase.getInstance().reference
    private val sendChatRef = mDatabase.child("chat")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        leaveChatService.setChatView(this)

        // 유저 정보 저장
        firstIndex = intent.getIntExtra("firstIndex", -1)
        secondIndex = intent.getIntExtra("secondIndex", -1)
        lastChatIdx = intent.getIntExtra("lastChatIdx", -1)
        chatIdx = intent.getStringExtra("chatRoomIndex").toString()

        binding.chatContentNicknameTv.text = intent.getStringExtra("nickname")
        profileImg = intent.getStringExtra("profile")!!
        nickname = intent.getStringExtra("nickname")!!

        clickListener()
        initAdapter(chatIdx)
        getChats()
    }

    private fun clickListener() {
        // 뒤로가기
        binding.chatContentBackIb.setOnClickListener{ finish() }

        // 메세지 보내기
        binding.chatContentSendTv.setOnClickListener {
            val chat = "${binding.chatContentTextEt.text.trim()}"
            if(chat.isNotEmpty()) {
                val timeStamp = System.currentTimeMillis()

                if(num == -1) createChatRoom()  // 그 전에 올린 채팅이 한 개도 없을 경우
                else writeChat(ChatComment(chat, false, timeStamp, getUserIdx()))
            }
        }

        // 팝업 메뉴 호출
        binding.chatContentMenuIv.setOnClickListener {
            showPopup(binding.chatContentMenuIv)
        }
    }

    private fun initAdapter(chatIdx : String) {
        chatRVAdapter = ChatContentRVAdapter(profileImg, nickname, chatIdx, getUserIdx())
        binding.chatContentRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.chatContentRv.adapter = chatRVAdapter

    }
    
    // 데이터를 올리는 부분
    private fun createChatRoom() {
        // 다른 유저 마이페이지에서 들어온 경우
        // 채팅방이 없는 상태면 파이어베이스에 올려주고 서버에도 채팅방 생성
        // 채팅방이 있는지 없는지 파악 여부는 comments가 1개인지로 파악
        sendChatRef.child(chatIdx).child("users").setValue(ChatUser(firstIndex, -1, secondIndex, -1))
            .addOnSuccessListener {
                Log.d("CREATE/SUC", chatIdx)
            }  // 채팅방 users 입력, 채팅방 생성
    }

    private fun writeChat(chatComment: ChatComment) {
        // child에 있는 path가 없는 경우 만들어주고 있는 경우는 path를 타고 들어가서 값을 파이어베이스에 넣어주는 형식
        Log.d("NUM/CHECK", num.toString())  // 추가 확인
        sendChatRef.child(chatIdx).child("comments").child("${num + 1}").setValue(chatComment)
            .addOnSuccessListener {
                binding.chatContentTextEt.setText("")
                if(binding.chatContentTextEt.isFocused) {  // 다 올라갔으면 내용 초기화 후 키보드 내려주기
                    hideKeyboard()
                }
            }
    }

    private fun getChats() {
        // 게시물에 달린 댓글 받아오기
        // 여기서 중요한 점 : 이 리스너는 onCreate에서 한 번만 호출되어야 함
        // 필요할 때마다 불러오는 게 아님 <- 변화를 감지하는 리스너기 때문
        // 처음 화면을 열면 무조건 한 번 실행돼서 초기 값 받아올 수 있음
        // 데이터를 받아오는 것은 비동기적으로 진행되기 때문에 return 값은 무조건 null, size를 세는 것도 안됨-
        getChatRef.child(chatIdx).child("comments").addValueEventListener(object : ValueEventListener {  // 데베에 변화가 있으면 새로 불러옴
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    num = -1
                    chatRVAdapter.clearChat()  // 새로 불러오기 때문에 초기화 필요

                    for (commentSnapShot in snapshot.children) {  // 하나씩 불러옴
                        val getData = commentSnapShot.getValue(ChatComment::class.java)

                        if (getData != null) {
                            if(lastChatIdx <= num) {
                                if (getData.userIdx == getUserIdx()) {
                                    val chat: ChatComment = getData
                                    chat.type = 1
                                    chatRVAdapter.addNewChat(chat)  // 리사이클러뷰에 채팅을 한 개씩 추가
                                    Log.d("FIRE/SUC", getData.toString())  // 추가 확인
                                    Log.d("FIRE-NUM/SUC", num.toString())  // 추가 확인
                                } else {
                                    val chat: ChatComment = getData
                                    chat.type = 0
                                    chatRVAdapter.addNewChat(chat)  // 리사이클러뷰에 채팅을 한 개씩 추가
                                    Log.d("FIRE/SUC", getData.toString())  // 추가 확인
                                    Log.d("FIRE-NUM/SUC", num.toString())  // 추가 확인
                                }

                                binding.chatContentRv.scrollToPosition(chatRVAdapter.itemCount - 1)
                            }
                        }
                        num++
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FAIL", "데이터를 불러오지 못했습니다")
            }
        })
    }

    private fun showPopup(view: View) {
        val themeWrapper = ContextThemeWrapper(applicationContext, R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.chat_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener{item->
            if (item!!.itemId== R.id.chat_delete) {
                // 채팅방 나가기 APi 호출
                leaveChatService.leaveChat(getJwt()!!, chatIdx, LastChatIdx(num))
                Log.d("LAST-CHAT-IDX", "$num")
            }

            false
        }
        popupMenu.show()
    }

    // 채팅방 나가기 API
    override fun onLeaveSuccess(result: String) {
        Log.d("LEAVE/SUC", result)
        finish()
    }

    override fun onLeaveFailure(code: Int, message: String) {
        Log.d("LEAVE/FAIL", "$code $message")
    }

    /* 추가 기능을 위한 함수들 */
    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        // EditText를 제외한 영역을 누르면 키보드를 내려줌
        val focusView = currentFocus
        if (focusView != null && ev != null) {
            val rect = Rect()
            focusView.getGlobalVisibleRect(rect)
            val x = ev.x.toInt()
            val y = ev.y.toInt()

            if (!rect.contains(x, y)) {
                val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputMethodManager.hideSoftInputFromWindow(focusView.windowToken, 0)
                focusView.clearFocus()
            }
        }
        return super.dispatchTouchEvent(ev)
    }
}