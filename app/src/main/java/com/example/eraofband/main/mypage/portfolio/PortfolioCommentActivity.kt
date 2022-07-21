package com.example.eraofband.main.mypage.portfolio

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Comment
import com.example.eraofband.databinding.ActivityPortfolioCommentBinding
import com.example.eraofband.main.usermypage.UserMyPageFragment
import com.example.eraofband.remote.portfolio.PofolCommentResult
import com.example.eraofband.remote.portfolio.PofolCommentService
import com.example.eraofband.remote.portfolio.PofolCommentView
import com.example.eraofband.remote.portfolio.PofolCommentWriteResult



class PortfolioCommentActivity : AppCompatActivity(), PofolCommentView {

    private lateinit var binding : ActivityPortfolioCommentBinding

    private val commentService = PofolCommentService()
    private val commentRVAdapter = PortfolioCommentRVAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioCommentBackIv.setOnClickListener { finish() }  // 뒤로 가기


        textWatcher()  // 댓글 창에 뭐가 있는지 확인하는 용도, 입력 색을 바꾸기 위해
    }

    override fun onStart() {
        super.onStart()

        commentService.setCommentView(this)
        commentService.getComment(intent.getIntExtra("pofolIdx", 0))
    }

    private fun initRecyclerView(item: List<PofolCommentResult>) {
        binding.portfolioCommentCommentRv.adapter = commentRVAdapter
        binding.portfolioCommentCommentRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        commentRVAdapter.initComment(item)

        commentRVAdapter.setMyItemClickListener(object : PortfolioCommentRVAdapter.MyItemClickListener {
            // 팝업 메뉴 띄우기
            override fun onShowPopUp(commentIdx: Int, position: Int, userIdx: Int, view: View) {
                if(userIdx == getUserIdx()) showMyPopup(commentIdx, position, view)  // 내가 단 댓글
                else showOtherPopup(commentIdx, position, view)  // 다른 사람이 단 댓글
            }

            override fun onItemClick() {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.main_frm, UserMyPageFragment()).commitAllowingStateLoss()
            }
        })
    }

    private fun textWatcher() {  // 댓글 입력 감시
        binding.portfolioCommentWriteEt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 입력이 시작되기 전에 작동
                Log.d("TEXTWATCHER", "START")
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // 입력이 시작되면 작동
                if(binding.portfolioCommentWriteEt.text.isNotEmpty()) {
                    binding.portfolioCommentUploadTv.setTextColor(Color.parseColor("#1864FD"))
                }
                else {
                    binding.portfolioCommentUploadTv.setTextColor(Color.parseColor("#7FA8FF"))
                }
            }

            override fun afterTextChanged(text: Editable?) {
                // 입력이 끝난 후에 작동
                Log.d("TEXTWATCHER", "END")
            }
        })

        binding.portfolioCommentUploadTv.setOnClickListener {
            if(binding.portfolioCommentWriteEt.text.isNotEmpty()) {  // 댓글에 적은 내용이 있는 경우 댓글 업로드
                commentService.writeComment(getJwt()!!, intent.getIntExtra("pofolIdx", 0), Comment(binding.portfolioCommentWriteEt.text.toString(), getUserIdx()))
            }
        }
    }

    private fun getUserIdx() : Int {  // 내 userIdx를 불러옴
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getInt("userIdx", 0)
    }

    private fun getJwt() : String? {  // 내 jwt를 불러옴
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun showMyPopup(commentIdx: Int, position: Int, view: View) {  // 내 댓글인 경우 삭제 가능
        val themeWrapper = ContextThemeWrapper(applicationContext , R.style.MyPopupMenu)
        val popupMenu = PopupMenu(themeWrapper, view, Gravity.END, 0, R.style.MyPopupMenu)
        popupMenu.menuInflater.inflate(R.menu.my_comment_menu, popupMenu.menu) // 메뉴 레이아웃 inflate

        popupMenu.setOnMenuItemClickListener { item ->
            if (item!!.itemId == R.id.my_comment_delete) {
                // position을 넘겨줌 이거 말고 생각이 안나요ㅠㅠ
                val commentSP = getSharedPreferences("comment", MODE_PRIVATE)
                val editor = commentSP.edit()

                editor.putInt("position", position)
                editor.apply()

                // 댓글 삭제
                commentService.deleteComment(getJwt()!!, commentIdx, getUserIdx())
            }

            false
        }

        popupMenu.show() // 팝업 보여주기
    }

    private fun showOtherPopup(commentIdx: Int, position: Int, view: View) {  // 다른 사람 댓글인 경우 신고만 가능
        val popup = androidx.appcompat.widget.PopupMenu(applicationContext, view) // PopupMenu 객체 선언
        popup.menuInflater.inflate(R.menu.other_comment_menu, popup.menu) // 메뉴 레이아웃 inflate

        popup.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.other_comment_report) {
                Log.d("REPORT", "COMMENT")
            }

            false
        }

        popup.show() // 팝업 보여주기
    }

    override fun onCommentSuccess(code: Int, result: List<PofolCommentResult>) {
        Log.d("GETCOMMENT/SUC", result.toString())
        initRecyclerView(result)  // 댓글 목록을 불러옴
    }

    override fun onCommentFailure(code: Int, message: String) {
        Log.d("GETCOMMENT/FAIL", "$code $message")
    }

    override fun onCommentWriteSuccess(code: Int, result: PofolCommentWriteResult) {
        Log.d("WRITECOMMENT/SUC", result.toString())
        commentRVAdapter.addComment(PofolCommentResult(result.content, result.nickName, result.pofolCommentIdx, result.pofolIdx, result.profileImgUrl,result.updatedAt, result.userIdx))

        // 키보드 원상태로 되돌리기
        binding.portfolioCommentWriteEt.text = null
        binding.portfolioCommentUploadTv.setTextColor(Color.parseColor("#7FA8FF"))

        // 키보드 내리기
        val inputManager : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken!!, InputMethodManager.HIDE_NOT_ALWAYS)
    }

    override fun onCommentWriteFailure(code: Int, message: String) {
        Log.d("WRITECOMMENT/FAIL", message)
    }

    override fun onCommentDeleteSuccess(code: Int, result: String) {
        Log.d("DELETECOMMENT/SUC", result)
        val commentSP = getSharedPreferences("comment", MODE_PRIVATE)

        // 리사이클러뷰에서도 삭제
        commentRVAdapter.deleteComment(commentSP.getInt("position", 0))

        // 사용한 position은 지워줌
        val editor = commentSP.edit()
        editor.remove("position")
        editor.apply()
    }

    override fun onCommentDeleteFailure(code: Int, message: String) {
        Log.d("DELETECOMMENT/FAIL", message)
        }
    }