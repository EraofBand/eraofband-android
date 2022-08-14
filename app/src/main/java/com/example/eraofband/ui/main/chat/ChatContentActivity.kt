package com.example.eraofband.ui.main.chat

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.data.ChatComment
import com.example.eraofband.data.ChatUser
import com.example.eraofband.data.MakeChatRoom
import com.example.eraofband.databinding.ActivityChatContentBinding
import com.example.eraofband.remote.chat.makeChat.MakeChatService
import com.example.eraofband.remote.chat.makeChat.MakeChatView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*

class ChatContentActivity : AppCompatActivity(), MakeChatView {

    private lateinit var binding: ActivityChatContentBinding

    // 채팅방 인덱스
    private var chatIdx = ""
    private var num = 0

    // 채팅 내역을 받아오기 위한 파이어베이스
    private val database = Firebase.database
    private val getChatRef = database.getReference("chat")
    private val getChatRoomRef = getChatRef.child(chatIdx)

    // 파이어베이스로 값 올리기
    private var mDatabase = FirebaseDatabase.getInstance().reference
    private val sendChatRef = mDatabase.child("chat")
    private val sendChatRoomRef = sendChatRef.child(chatIdx)

    // 채팅방 생성 API
    private val makeChatService = MakeChatService()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        makeChatService.setChatView(this)
        if(intent.getStringExtra("chatRoomIdx").isNullOrEmpty()) chatIdx = "${UUID.randomUUID()}"
        else intent.getStringExtra("chatRoomIdx")

        binding.chatContentNicknameTv.text = intent.getStringExtra("name")

        binding.chatContentBackIb.setOnClickListener{ finish() }  // 뒤로가기

        binding.chatContentSendTv.setOnClickListener {  // 메세지 보내기
            val message = binding.chatContentTextEt.text.toString()
            val timeStamp = System.currentTimeMillis()
            writeChat(ChatComment(message, false, timeStamp, getUserIdx()))
        }

        getChats()
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getChats() {
        // 게시물에 달린 댓글 받아오기
        // 여기서 중요한 점 : 이 리스너는 onCreate에서 한 번만 호출되어야 함
        // 필요할 때마다 불러오는 게 아님 <- 변화를 감지하는 리스너기 때문
        // 처음 화면을 열면 무조건 한 번 실행돼서 초기 값 받아올 수 있음
        // 데이터를 받아오는 것은 비동기적으로 진행되기 때문에 return 값은 무조건 null, size를 세는 것도 안됨
        // 자세한 기능은 리사이클러뷰에서 진행해야할 것 같습니다
        getChatRoomRef.child("comments").addValueEventListener(object : ValueEventListener {  // 데베에 변화가 있으면 새로 불러옴
            override fun onDataChange(snapshot: DataSnapshot) {
                num = 0
//                "리사이클러뷰".clearChat()  // 새로 불러오기 때문에 초기화 필요
                if (snapshot.exists()){
                    for (commentSnapShot in snapshot.children){  // 하나씩 불러옴
                        val getData = commentSnapShot.getValue(ChatComment::class.java)  // 리스폰스가 들어가겠죵

                        if (getData != null) {
//                            "리사이클러뷰".addNewChat(getData)  // 리사이클러뷰에 채팅을 한 개씩 추가
                            num++
                            Log.d("SUCCESS", getData.toString())  // 추가 확인
                        }

                    }
                    if(num == 1) { createChatRoom() }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("FAIL", "데이터를 불러오지 못했습니다")
            }
        })
    }

    // 데이터를 올리는 부분
    private fun createChatRoom() {
        // 다른 유저 마이페이지에서 들어온 경우
        // 채팅방이 없는 상태면 파이어베이스에 올려주고 서버에도 채팅방 생성
        // 채팅방이 있는지 없는지 파악 여부는 comments가 1개인지로 파악
        val firstIndex = intent.getIntExtra("firstIndex", -1)
        val secondIndex = intent.getIntExtra("secondIndex", -1)

        sendChatRoomRef.child("users").setValue(ChatUser(firstIndex, secondIndex))
            .addOnSuccessListener {
                makeChatService.makeChat(MakeChatRoom(chatIdx, firstIndex, secondIndex))
            }  // 채팅방 users 입력, 채팅방 생성
    }

    private fun writeChat(chatComment: ChatComment) {
        sendChatRoomRef.child("comments").child("${num + 1}").setValue(chatComment)
            .addOnSuccessListener {
                if(binding.chatContentTextEt.isFocused) {  // 다 올라갔으면 키보드 내려주기
                    val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
                }
            }
        // child에 있는 path가 없는 경우 만들어주고 있는 경우는 path를 타고 들어가서 값을 파이어베이스에 넣어주는 형식
    }

    override fun onMakeSuccess(result: String) {
        Log.d("MAKE/SUC", result)
    }

    override fun onMakeFailure(code: Int, message: String) {
        Log.d("MAKE/SUC", "$code $message")
    }
}