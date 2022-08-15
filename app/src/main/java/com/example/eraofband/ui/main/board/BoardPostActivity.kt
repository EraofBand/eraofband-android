package com.example.eraofband.ui.main.board

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.ActivityBoardPostBinding
import com.example.eraofband.remote.board.getBoard.*
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

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

    private fun initCommentRV(item: List<GetBoardComments>) {
        commentRVAdapter = PostCommentRVAdapter(this)
        binding.boardPostCommentRv.adapter = commentRVAdapter
        binding.boardPostCommentRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        val comment = arrayListOf<GetBoardComments>()
        comment.addAll(item)
        comment.sortBy { it.groupNum }
        comment.reverse()
        Log.d("COMMENT", "$comment")

        commentRVAdapter.initComment(comment)

        commentRVAdapter.setMyItemClickListener(object : PostCommentRVAdapter.MyItemClickListener {
            // 팝업 메뉴 띄우기
            override fun onShowPopUp(commentIdx: Int, position: Int, userIdx: Int, view: View) {
                showPopup(commentIdx, position, userIdx, view)
            }

            override fun onItemClick(comment: GetBoardComments) {
                if(comment.userIdx == getUserIdx()) {
                    startActivity(Intent(this@BoardPostActivity, MyPageActivity::class.java))
                }  // 만약 누른 유저가 본인일 경우
                else {
                    val intent = Intent(this@BoardPostActivity, UserMyPageActivity::class.java)
                    intent.putExtra("userIdx", comment.userIdx)
                    startActivity(intent)
                }  // 다른 유저일 경우
            }
        })
    }

    private fun showPopup(commentIdx: Int, position: Int, userIdx: Int, view: View) {  // 내 댓글인 경우 삭제 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.comment_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.comment_delete) {  // 댓글 삭제하기
                // position을 넘겨줌 이거 말고 생각이 안나요ㅠㅠ
                val commentSP = getSharedPreferences("comment", MODE_PRIVATE)
                val editor = commentSP.edit()

                editor.putInt("position", position)
                editor.apply()

                // 댓글 삭제
//                commentService.deleteComment(getJwt()!!, commentIdx, getUserIdx())
            }
            else {  // 댓글 신고하기
                Log.d("REPORT", "COMMENT")
            }

            false
        }

        if(userIdx == getUserIdx()) {
            popupMenu.menu.setGroupVisible(R.id.comment_report_gr, false)
        }
        else {
            popupMenu.menu.setGroupVisible(R.id.comment_delete_gr, false)
        }

        popupMenu.show() // 팝업 보여주기
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun getUserIdx() : Int {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
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

        initCommentRV(result.getBoardComments)
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