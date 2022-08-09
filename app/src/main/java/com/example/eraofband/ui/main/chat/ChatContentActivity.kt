package com.example.eraofband.ui.main.chat

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.eraofband.data.ChatComment
import com.example.eraofband.data.ChatUser
import com.example.eraofband.databinding.ActivityChatContentBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ChatContentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatContentBinding

    // 채팅 목록을 받아오기 위한 파이어베이스
    private val database = Firebase.database
    private val chatRef = database.getReference("chat")

    // 파이어베이스로 값 올리기
    private lateinit var mDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityChatContentBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }

    // 일단 어디에 이 함수들을 써야할 지 모르겠어서 여기에 다 모아놨어요
    private fun getChats() {
        // 게시물에 달린 댓글 받아오기
        // 여기서 중요한 점 : 이 리스너는 onCreate에서 한 번만 호출되어야 함
        // 필요할 때마다 불러오는 게 아님 <- 변화를 감지하는 리스너기 때문
        // 자세한 기능은 리사이클러뷰에서 진행해야할 것 같습니다
//        chatRef.addValueEventListener(object : ValueEventListener {  // 데베에 변화가 있으면 새로 불러옴
//            override fun onDataChange(snapshot: DataSnapshot) {
//                "리사이클러뷰".clearChat()  // 새로 불러오기 때문에 초기화 필요
//
//                if (snapshot.exists()){
//                    for (commentSnapShot in snapshot.children){  // 하나씩 불러옴
//                        val getData = commentSnapShot.getValue("데이터클래스"::class.java)  // 리스폰스가 들어가겠죵
//
//                        if (getData != null) {
//                            "리사이클러뷰".addNewChat(getData)  // 리사이클러뷰에 채팅을 한 개씩 추가
//                            Log.d("SUCCESS", getData.toString())  // 추가 확인
//                        }
//
//                    }
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Log.d("FAIL", "데이터를 불러오지 못했습니다")
//            }
//        })
        }


        // 데이터를 올리는 부분
        private fun createChatRoom() {
            // API로 스웨거에서 채팅룸 idx, 유저 idx들을 받아오면 이 함수 실행
            mDatabase.child("chat").setValue("채팅방 idx")  // 채팅방 생성
            mDatabase.child("chat").child("채팅방 idx").child("users")
                .setValue(ChatUser(0, 1))  // 채팅방 users 입력
            // child 부분은 위에 채팅방 idx만 확실하게 알 수 있다면 ref로 정의해서 더 깔끔하게 넣을 수 있어용 !!
        }

        private fun writeChat() {
            mDatabase.child("chat").child("채팅방 idx").child("comments")
                .child("유저인덱스${System.currentTimeMillis()}").setValue(
                ChatComment(
                    "음",
                    false,
                    System.currentTimeMillis().toInt(),
                    1
                )
            )  // 채팅방 users 입력
            // 인덱싱은 유저인덱스+타임스탬프
            // 한 유저가 같은 밀리 초 안에 채팅을 보내는 건 어려울 거라고 생각하기 때문에 괜찮을 거라고 생각합니다 아닐 수도 있어요
            // setValue로 값을 넣어주면 됩니다
            // child에 있는 path가 없는 경우 만들어주고 있는 경우는 path를 타고 들어가서 값을 파이어베이스에 넣어주는 형식
        }
}
