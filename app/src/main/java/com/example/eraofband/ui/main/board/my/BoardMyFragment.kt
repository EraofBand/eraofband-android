package com.example.eraofband.ui.main.board.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.R
import com.example.eraofband.databinding.FragmentBoardMyBinding
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListResult
import com.example.eraofband.remote.board.getMyBoardList.GetMyBoardListView
import com.example.eraofband.remote.board.getMyCommentList.GetMyCommentListService
import com.example.eraofband.remote.board.getMyCommentList.GetMyCommentListView
import com.example.eraofband.remote.board.getMyMyBoardList.GetMyBoardListService

class BoardMyFragment : Fragment(), GetMyBoardListView, GetMyCommentListView {
    private var _binding: FragmentBoardMyBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지
    private lateinit var mAdapter: BoardMyRVAdapter
    private val writeService = GetMyBoardListService()
    private val commentService = GetMyCommentListService()

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
            binding.boardWritingTv.setBackgroundResource(R.drawable.blue_round_bg2)  // 파란색
            binding.boardCommentTv.setBackgroundResource(R.drawable.gray_round_bg)  // 회색
            writeService.getMyBoardList(getJwt()!!)
        }
        binding.boardCommentTv.setOnClickListener {
            binding.boardWritingTv.setBackgroundResource(R.drawable.gray_round_bg)  // 회색
            binding.boardCommentTv.setBackgroundResource(R.drawable.blue_round_bg2)  // 파란색
            commentService.getMyCommentList(getJwt()!!)
        }
    }

    private fun connectAdapter(list: ArrayList<GetMyBoardListResult>) {
        mAdapter = BoardMyRVAdapter()
        binding.boardMyRv.adapter = mAdapter
        binding.boardMyRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.initBoardList(list)

//        mAdapter.setMyItemClickListener(object : BoardFreeRVAdapter.MyItemClickListener {
//            override fun onItemClick(boardIdx: String) {
//                val intent = Intent(activity, 게시물 상세보기 액티비티::class.java)
//                intent.putExtra("boardIdx", boardIdx)
//                startActivity(intent)
//            }
//        })
    }

    private fun getJwt() : String? {
        val userSP = requireActivity().getSharedPreferences("user", AppCompatActivity.MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    override fun onGetListSuccess(result: ArrayList<GetMyBoardListResult>) {
        Log.d("GET BOARD LIST / SUCCESS", result.toString())
        connectAdapter(result)
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }

    override fun onGetCListSuccess(result: ArrayList<GetMyBoardListResult>) {
        Log.d("GET BOARD LIST / SUCCESS", result.toString())
        connectAdapter(result)
    }

    override fun onGetCListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}