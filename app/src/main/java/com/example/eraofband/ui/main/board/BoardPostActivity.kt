package com.example.eraofband.ui.main.board

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.ActivityBoardPostBinding
import com.google.android.material.tabs.TabLayoutMediator

class BoardPostActivity: AppCompatActivity() {

    private lateinit var binding: ActivityBoardPostBinding

    private lateinit var commentRVAdapter: PostCommentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBoardPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVP()
    }

    private fun initVP() {
        // 게시물 사진 연결
        val postVPAdapter = PostVPAdapter(this)
        binding.boardPostPictureVp.adapter = postVPAdapter

        TabLayoutMediator(binding.boardPostPictureTb, binding.boardPostPictureVp) { _, _ ->

        }.attach()
        binding.boardPostPictureTb.visibility = View.VISIBLE
//        if(feed.contentsList.size == 1) binding.boardPostPictureTb.visibility = View.GONE
//        else binding.boardPostPictureTb.visibility = View.VISIBLE
//
//        for(i in 0 until feed.contentsList.size) {
//            feedVPAdapter.addImage(feed.contentsList[i].contentsUrl)
//        }
    }

    private fun initCommentRV() {
        commentRVAdapter = PostCommentRVAdapter(this)
        binding.boardPostCommentRv.adapter = commentRVAdapter
        binding.boardPostCommentRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//
//        commentRVAdapter.initComment(item)

//        commentRVAdapter.setMyItemClickListener(object : PortfolioCommentRVAdapter.MyItemClickListener {
//            // 팝업 메뉴 띄우기
//            override fun onShowPopUp(commentIdx: Int, position: Int, userIdx: Int, view: View) {
//                showPopup(commentIdx, position, userIdx, view)
//            }
//
//            override fun onItemClick(comment: PofolCommentResult) {
//                if(comment.userIdx == getUserIdx()) {
//                    startActivity(Intent(this@PortfolioCommentActivity, MyPageActivity::class.java))
//                }  // 만약 누른 유저가 본인일 경우
//                else {
//                    val intent = Intent(this@PortfolioCommentActivity, UserMyPageActivity::class.java)
//                    intent.putExtra("userIdx", comment.userIdx)
//                    startActivity(intent)
//                }  // 다른 유저일 경우
//            }
//        })
    }
}