package com.example.eraofband.ui.main.board.info

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.data.Comment
import com.example.eraofband.data.Reply
import com.example.eraofband.databinding.ActivityBoardPostBinding
import com.example.eraofband.remote.board.boardComment.BoardCommentService
import com.example.eraofband.remote.board.boardComment.BoardCommentView
import com.example.eraofband.remote.board.boardLike.BoardLikeResult
import com.example.eraofband.remote.board.boardLike.BoardLikeService
import com.example.eraofband.remote.board.boardLike.BoardLikeView
import com.example.eraofband.remote.board.deleteBoard.DeleteBoardService
import com.example.eraofband.remote.board.deleteBoard.DeleteBoardView
import com.example.eraofband.remote.board.getBoard.*
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity
import com.example.eraofband.ui.report.ReportDialog

class BoardPostActivity: AppCompatActivity(), GetBoardView, BoardCommentView, BoardLikeView, DeleteBoardView {

    private lateinit var binding: ActivityBoardPostBinding

    private var boardIdx = -1
    private var userIdx = -1

    private val likeService = BoardLikeService()
    private var like = false
    private var likeCnt = 0

    private lateinit var commentRVAdapter: PostCommentRVAdapter
    private val commentService = BoardCommentService()
    private var commentPosition = -1
    private var groupNum = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityBoardPostBinding.inflate(layoutInflater)
        setContentView(binding.root)

        boardIdx = intent.getIntExtra("boardIdx", -1)
        likeService.setLikeView(this)
        commentService.setBoardView(this)
        textWatcher()

        binding.boardPostTopBackIv.setOnClickListener { finish() }  // 뒤로가기

        binding.boardPostProfileIv.setOnClickListener {
            val intent = Intent(this, UserMyPageActivity::class.java)
            intent.putExtra("userIdx", userIdx)
            startActivity(intent)
        }

        binding.boardPostNameTv.setOnClickListener {
            val intent = Intent(this, UserMyPageActivity::class.java)
            intent.putExtra("userIdx", userIdx)
            startActivity(intent)
        }

        binding.boardPostTopMoreIv.setOnClickListener { showPopup(binding.boardPostTopMoreIv) }

        binding.boardPostLikeIv.setOnClickListener {
            if(like) likeService.deleteLike(getJwt()!!, boardIdx)
            else likeService.like(getJwt()!!, boardIdx)
        }

        binding.boardPostWriteCommentUploadTv.setOnClickListener {
            val comment = binding.boardPostWriteCommentEt.text.toString()
            val userIdx = getUserIdx()
            if(comment.isNotEmpty()) {  // 댓글에 적은 내용이 있는 경우 댓글 업로드
                // 답글 어쩌구가 있으면 답글, 아무것도 없으면 댓글
                if(binding.boardPostWriteReplyInfoTv.visibility == View.VISIBLE) commentService.writeReply(getJwt()!!, boardIdx, Reply(comment, groupNum, userIdx))
                else commentService.writeComment(getJwt()!!, boardIdx, Comment(comment, userIdx))
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val boardService = GetBoardService()
        boardService.setBoardView(this)
        boardService.getBoard(getJwt()!!, boardIdx)
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
        comment.sortByDescending { it.groupNum }
        comment.reverse()
        Log.d("COMMENT", "$comment")

        commentRVAdapter.initComment(comment)

        commentRVAdapter.setMyItemClickListener(object : PostCommentRVAdapter.MyItemClickListener {
            // 팝업 메뉴 띄우기
            override fun onShowPopUp(commentIdx: Int, position: Int, userIdx: Int, view: View) {
                showCommentPopup(commentIdx, position, userIdx, view)
            }

            @SuppressLint("SetTextI18n")
            override fun onWriteReply(commentIdx: Int, name: String, position: Int) {
                binding.boardPostWriteReplyInfoTv.text = "$name${getString(R.string.write_reply_info)}"
                groupNum = commentIdx
                commentPosition = position

                binding.boardPostWriteReplyInfoTv.visibility = View.VISIBLE
                binding.boardPostWriteReplyDeleteIv.visibility = View.VISIBLE
                showKeyboard()

                binding.boardPostWriteReplyDeleteIv.setOnClickListener {
                    binding.boardPostWriteCommentEt.setText("")
                    binding.boardPostWriteReplyInfoTv.visibility = View.GONE
                    binding.boardPostWriteReplyDeleteIv.visibility = View.GONE

                    if(binding.boardPostWriteCommentEt.isFocused) hideKeyboard()
                }
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

    private fun showPopup(view: View) {  // 내 댓글인 경우 삭제 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.board_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if(item!!.itemId == R.id.board_edit) {
                // 수정 창 띄우기
            }
            else if (item.itemId == R.id.board_delete) {  // 게시물 삭제하기
                val deleteService = DeleteBoardService()
                deleteService.setDeleteView(this)
                deleteService.deleteBoard(getJwt()!!, boardIdx, getUserIdx())
            }
            else {  // 게시물 신고하기
                val reportDialog = ReportDialog(getJwt()!!, 5, boardIdx, getUserIdx())
                reportDialog.isCancelable = false
                reportDialog.show(supportFragmentManager, "report")
            }

            false
        }

        if(userIdx == getUserIdx()) {
            popupMenu.menu.setGroupVisible(R.id.board_report_gr, false)
        }
        else {
            popupMenu.menu.setGroupVisible(R.id.board_edit_gr, false)
            popupMenu.menu.setGroupVisible(R.id.board_delete_gr, false)
        }

        popupMenu.show() // 팝업 보여주기
    }

    private fun showCommentPopup(commentIdx: Int, position: Int, userIdx: Int, view: View) {  // 내 댓글인 경우 삭제 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.comment_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.comment_delete) {  // 댓글 삭제하기
                commentPosition = position
                commentService.deleteComment(getJwt()!!, commentIdx, getUserIdx())
            }
            else {  // 댓글 신고하기
                val reportDialog = ReportDialog(getJwt()!!, 6, commentIdx, getUserIdx())
                reportDialog.isCancelable = false
                reportDialog.show(supportFragmentManager, "report")
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

    private fun textWatcher() {  // 댓글 입력 감시
        binding.boardPostWriteCommentEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 입력이 시작되기 전에 작동
                binding.boardPostWriteCommentEt.hint = getString(R.string.enter_message)
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 입력이 시작되면 작동
                if(binding.boardPostWriteCommentEt.text.isNotEmpty()) {
                    binding.boardPostWriteCommentUploadTv.setTextColor(Color.parseColor("#1864FD"))
                }
                else {
                    binding.boardPostWriteCommentUploadTv.setTextColor(Color.parseColor("#7FA8FF"))
                }
            }

            override fun afterTextChanged(text: Editable?) {
                // 입력이 끝난 후에 작동
                binding.boardPostWriteCommentEt.hint = getString(R.string.enter_message)
            }
        })
    }

    private fun hideKeyboard() {
        val inputManager: InputMethodManager = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(this.currentFocus!!.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    private fun showKeyboard() {
        binding.boardPostWriteCommentEt.requestFocus()
        val inputManager : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.showSoftInput(binding.boardPostWriteCommentEt, InputMethodManager.SHOW_IMPLICIT)
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
        Glide.with(this).load(result.profileImgUrl)  // 프로필 사진
            .apply(RequestOptions.centerCropTransform()).apply(RequestOptions.circleCropTransform())
            .into(binding.boardPostProfileIv)

        userIdx = result.userIdx

        binding.boardPostTopTitleTv.text = getCategory(result.category)  // 게시판 설정

        binding.boardPostNameTv.text = result.nickName  // 닉네임
        binding.boardPostTimeTv.text = result.updatedAt  // 게시물을 올린 시간

        initVP(result.getBoardImgs)  // 게시물 사진
        binding.boardPostTitleTv.text = result.title  // 게시물 제목
        binding.boardPostContentTv.text = result.content  // 게시물 본문

        likeCnt = result.boardLikeCount
        binding.boardPostLikeTv.text = "${getString(R.string.like)} $likeCnt"  // 좋아요
        binding.boardPostCommentTv.text = "${getString(R.string.comment)} ${result.commentCount}"  // 댓글
        binding.boardPostViewTv.text = "${getString(R.string.view_cnt)} ${result.views}"  // 조회수

        like = if(result.likeOrNot == "Y") {
                    binding.boardPostLikeIv.setImageResource(R.drawable.ic_heart_on)
                    true
                } else {
                    binding.boardPostLikeIv.setImageResource(R.drawable.ic_heart_off)
                    false
                }

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

    override fun onWriteCommentSuccess(result: GetBoardComments) {
        Log.d("WRITE/SUC", "$result")
        if(binding.boardPostWriteReplyInfoTv.visibility == View.GONE) {
            commentRVAdapter.addComment(result)

            binding.boardPostWriteCommentEt.setText("")
            hideKeyboard()
        }
        else {
            commentRVAdapter.addReply(commentPosition, result)
            commentPosition = -1

            binding.boardPostWriteCommentEt.setText("")
            hideKeyboard()
            binding.boardPostWriteReplyInfoTv.visibility = View.GONE
            binding.boardPostWriteReplyDeleteIv.visibility = View.GONE
        }
    }

    override fun onWriteCommentFailure(code: Int, message: String) {
        Log.d("WRITE/FAIL", "$code $message")
    }

    override fun onDeleteCommentSuccess(result: String) {
        Log.d("DELETE/SUC", result)
        commentRVAdapter.deleteComment(commentPosition)
        commentPosition = -1  // position 값 초기화 <- 엉뚱한 댓글이 삭제되지 않도록
    }

    override fun onHaveReply(code: Int, message: String) {
        Log.d("DELETE/FAIL", "$code $message")
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()  // 삭제 불가능하다는 토스트 메세지를 띄워줌
    }

    override fun onDeleteCommentFailure(code: Int, message: String) {
        Log.d("DELETE/FAIL", "$code $message")
    }

    @SuppressLint("SetTextI18n")
    override fun onLikeSuccess(result: BoardLikeResult) {
        Log.d("LIKE/SUC", "$result")
        binding.boardPostLikeIv.setImageResource(R.drawable.ic_heart_on)  // 좋아요 완료
        likeCnt++
        binding.boardPostLikeTv.text = "${getString(R.string.like)} $likeCnt"
        like = true
    }

    override fun onLikeFailure(code: Int, message: String) {
        Log.d("LIKE/FAIL", "$code $message")
    }

    @SuppressLint("SetTextI18n")
    override fun onDeleteLikeSuccess(result: String) {
        Log.d("DELETE/SUC", result)
        binding.boardPostLikeIv.setImageResource(R.drawable.ic_heart_off)  // 좋아요 취소 완료
        likeCnt--
        binding.boardPostLikeTv.text = "${getString(R.string.like)} $likeCnt"
        like = false
    }

    override fun onDeleteLikeFailure(code: Int, message: String) {
        Log.d("DELETE/FAIL", "$code $message")
    }

    override fun onDeleteSuccess(result: String) {
        Log.d("DELETE/SUC", result)
        finish()
    }

    override fun onDeleteFailure(code: Int, message: String) {
        Log.d("DELETE/FAIL", "$code $message")
    }
}