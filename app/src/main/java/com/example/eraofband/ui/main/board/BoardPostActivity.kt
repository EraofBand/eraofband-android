package com.example.eraofband.ui.main.board

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityBoardPostBinding
import com.example.eraofband.remote.board.getBoard.GetBoardImgs
import com.example.eraofband.remote.board.getBoard.GetBoardResult
import com.example.eraofband.remote.board.getBoard.GetBoardService
import com.example.eraofband.remote.board.getBoard.GetBoardView

class BoardPostActivity: AppCompatActivity(), GetBoardView {

    private lateinit var binding: ActivityBoardPostBinding

    private lateinit var commentRVAdapter: PostCommentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBoardPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.boardPostTopBackIv.setOnClickListener { finish() }  // 뒤로가기

    }

    override fun onResume() {
        super.onResume()
        val boardService = GetBoardService()
        boardService.setBandView(this)
        boardService.getBoard(getJwt()!!, 9)
    }

    private fun initVP(img: List<GetBoardImgs>) {
        // 게시물 사진 연결
        val postVPAdapter = PostVPAdapter(this)
        binding.boardPostPictureVp.adapter = postVPAdapter
        binding.boardPostPictureIndicator.attachTo(binding.boardPostPictureVp)

        if(img.isEmpty()) {
            binding.boardPostPictureVp.visibility = View.GONE
            binding.boardPostPictureIndicator.visibility = View.GONE
            return
        }  // 이미지가 없으면 더 이상의 과정 필요 X

        if(img.size == 1) binding.boardPostPictureIndicator.visibility = View.GONE

        for(i in img.indices) {
            postVPAdapter.addImage(img[i].imgUrl)
        }
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

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    @SuppressLint("SetTextI18n")
    override fun onGetSuccess(result: GetBoardResult) {
        Log.d("GET/SUC", "$result")

        binding.boardPostTopTitleTv.text = getCategory(result.category)  // 게시판 설정

        binding.boardPostNameTv.text = result.nickName  // 닉네임
        binding.boardPostTimeTv.text = result.updatedAt  // 게시물을 올린 시간

        initVP(result.getBoardImgs)  // 게시물 사진
        binding.boardPostTitleTv.text = result.title  // 게시물 제목
        binding.boardPostContentTv.text = result.content  // 게시물 본문

        binding.boardPostLikeTv.text = "${getString(R.string.like)} ${result.boardLikeCount}"  // 좋아요
        binding.boardPostCommentTv.text = "${getString(R.string.comment)} ${result.commentCount}"  // 댓글
        binding.boardPostViewTv.text = "${getString(R.string.view_cnt)} ${result.views}"  // 조회수
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GET/FAIL", "$code $message")
    }

    private fun getCategory(category: Int): String {
        return when(category) {
            0 -> getString(R.string.free_board)
            1 -> getString(R.string.question_board)
            2 -> getString(R.string.promotion_board)
            else -> getString(R.string.sell_board)
        }
   }
}