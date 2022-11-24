package com.example.eraofband.ui.main.board.my

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentBoardMyBinding
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListResult
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListView
import com.example.eraofband.remote.board.getMyCommentList.GetMyCommentListService
import com.example.eraofband.remote.board.getMyCommentList.GetMyCommentListView
import com.example.eraofband.remote.board.getMyMyBoardList.GetMyBoardListService
import com.example.eraofband.ui.main.board.info.BoardPostActivity

class BoardMyFragment : Fragment(), GetMyBoardListView, GetMyCommentListView {
    private var _binding: FragmentBoardMyBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var mAdapter: BoardMyRVAdapter

    private val writeService = GetMyBoardListService()
    private val commentService = GetMyCommentListService()

    private var write = true
    private var add = false
    private var loading = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardMyBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        writeService.setMyBoardListView(this)
        commentService.setMyCommentListView(this)

        binding.boardWritingTv.setBackgroundResource(R.drawable.blue_round_bg2)
        binding.boardCommentTv.setBackgroundResource(R.drawable.gray_round_bg)
        writeService.getMyBoardList(getJwt()!!)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.boardWritingTv.setOnClickListener {
            if(!write) {
                binding.boardWritingTv.setBackgroundResource(R.drawable.blue_round_bg2)  // 파란색
                binding.boardCommentTv.setBackgroundResource(R.drawable.gray_round_bg)  // 회색
                writeService.getMyBoardList(getJwt()!!)

                write = true
            }
            binding.boardMyRv.smoothScrollToPosition(0)  // 상단으로 이동
        }
        binding.boardCommentTv.setOnClickListener {
            if(write) {
                binding.boardWritingTv.setBackgroundResource(R.drawable.gray_round_bg)  // 회색
                binding.boardCommentTv.setBackgroundResource(R.drawable.blue_round_bg2)  // 파란색
                commentService.getMyCommentList(getJwt()!!)

                write = false
            }
            binding.boardMyRv.smoothScrollToPosition(0)  // 상단으로 이동
        }
    }

    private fun connectAdapter(list: ArrayList<GetMyBoardListResult>) {
        mAdapter = BoardMyRVAdapter()
        binding.boardMyRv.adapter = mAdapter
        binding.boardMyRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.initBoardList(list)

        mAdapter.setMyItemClickListener(object : BoardMyRVAdapter.MyItemClickListener {
            override fun onItemClick(boardIdx: Int) {
                val intent = Intent(activity, BoardPostActivity::class.java)
                intent.putExtra("boardIdx", boardIdx)
                startActivity(intent)
            }
        })

        binding.boardMyRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (binding.boardMyRv.canScrollVertically(1)) {  // 맨 위
                    Log.d("SCROLL", "TOP")
                }
                else if (binding.boardMyRv.canScrollVertically(-1)) {  // 맨 아래
                    Log.d("SCROLL", "BOTTOM")
                    Log.d("SCROLL / SUCCESS", "${mAdapter.itemCount}")

                    if(mAdapter.itemCount % 20 == 0) {
                        if(!loading) {
                            add = true
                            if(write) writeService.getMyBoardList(getJwt()!!)
                            else commentService.getMyCommentList(getJwt()!!)

                            loading = true
                        }
                    }
                }
                else {
                    Log.d("SCROLL", "IDLE")
                }
            }
        })
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetListSuccess(result: ArrayList<GetMyBoardListResult>) {
        // 내가 올린 글
        Log.d("GET BOARD LIST / SUCCESS", result.toString())
        if (!add) connectAdapter(result)
        else mAdapter.initBoardList(result)
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetCListSuccess(result: ArrayList<GetMyBoardListResult>) {
        // 댓글 단 글
        Log.d("GET BOARD LIST / SUCCESS", result.toString())
        if (!add) connectAdapter(result)
        else mAdapter.initBoardList(result)
    }

    override fun onGetCListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }
}