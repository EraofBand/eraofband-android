package com.example.eraofband.main.mypage.portfolio

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.data.Comment
import com.example.eraofband.databinding.ActivityPortfolioCommentBinding
import com.example.eraofband.main.MainActivity
import com.example.eraofband.main.mypage.usermypage.UserMyPageFragment

class PortfolioCommentActivity : AppCompatActivity() {

    private lateinit var binding : ActivityPortfolioCommentBinding
    private var commentList = arrayListOf(
        Comment("정말 대단해요!", 0),
        Comment("정말 대단해요!", 0),
        Comment("정말 대단해요!", 0),
        Comment("정말 대단해요!", 0),
        Comment("정말 대단해요!", 0),
        Comment("정말 대단해요!", 0)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPortfolioCommentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.portfolioCommentBackIv.setOnClickListener { finish() }  // 뒤로 가기


        textWatcher()  // 댓글 창에 뭐가 있는지 확인하는 용도, 입력 색을 바꾸기 위해
    }

    override fun onStart() {
        super.onStart()

        initRecyclerView()
    }

    private fun initRecyclerView() {
        val commentRVAdapter = PortfolioCommentRVAdapter(commentList)
        binding.portfolioCommentCommentRv.adapter = commentRVAdapter
        binding.portfolioCommentCommentRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        commentRVAdapter.setMyItemClickListener(object : PortfolioCommentRVAdapter.MyItemClickListener {
            override fun onItemClick(){
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
            if(binding.portfolioCommentWriteEt.text.isNotEmpty()) {
                // 댓글 달기 창 원래대로 되돌리기
                binding.portfolioCommentWriteEt.text = null
                binding.portfolioCommentUploadTv.setTextColor(Color.parseColor("#7FA8FF"))

                // 키보드 내리기
                val inputManager : InputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus!!.windowToken!!, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }
}