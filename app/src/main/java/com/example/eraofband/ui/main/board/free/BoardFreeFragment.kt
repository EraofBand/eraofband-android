package com.example.eraofband.ui.main.board.free

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.eraofband.databinding.FragmentBoardFreeBinding
import com.example.eraofband.remote.board.getBoardList.GetBoardListResponse
import com.example.eraofband.remote.board.getBoardList.GetBoardListResult
import com.example.eraofband.remote.board.getBoardList.GetBoardListService
import com.example.eraofband.remote.board.getBoardList.GetBoardListView
import com.example.eraofband.remote.user.userFollowList.FollowingInfo
import com.example.eraofband.ui.main.mypage.MyPageActivity
import com.example.eraofband.ui.main.mypage.follow.FollowingRVAdapter
import com.example.eraofband.ui.main.usermypage.UserMyPageActivity

class BoardFreeFragment : Fragment(), GetBoardListView {
    private var _binding: FragmentBoardFreeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지
    private lateinit var mAdapter: BoardFreeRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardFreeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val service = GetBoardListService()
        service.setBoardListView(this)
        service.getBoardList(0)
    }

    private fun connectAdapter(list: ArrayList<GetBoardListResult>) {
        mAdapter = BoardFreeRVAdapter()
        binding.boardFreeRv.adapter = mAdapter
        binding.boardFreeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.initBoardList(list)

//        mAdapter.setMyItemClickListener(object : BoardFreeRVAdapter.MyItemClickListener {
//            override fun onItemClick(boardIdx: String) {
//                val intent = Intent(activity, 게시물 상세보기 액티비티::class.java)
//                intent.putExtra("boardIdx", boardIdx)
//                startActivity(intent)
//            }
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetListSuccess(result: ArrayList<GetBoardListResult>) {
        Log.d("GET BOARD LIST / SUCCESS", result.toString())
        connectAdapter(result)
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }
}