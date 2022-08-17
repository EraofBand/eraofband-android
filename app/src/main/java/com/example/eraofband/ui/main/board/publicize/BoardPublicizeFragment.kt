package com.example.eraofband.ui.main.board.publicize

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentBoardPublicizeBinding
import com.example.eraofband.remote.board.getBoardList.GetBoardListResult
import com.example.eraofband.remote.board.getBoardList.GetBoardListService
import com.example.eraofband.remote.board.getBoardList.GetBoardListView

class BoardPublicizeFragment : Fragment(), GetBoardListView {
    private var _binding: FragmentBoardPublicizeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지
    private lateinit var mAdapter: BoardPublicizeRVAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardPublicizeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        val service = GetBoardListService()
        service.setBoardListView(this)
        service.getBoardList(2,0)
    }

    private fun connectAdapter(list: ArrayList<GetBoardListResult>) {
        mAdapter = BoardPublicizeRVAdapter()
        binding.boardPublicizeRv.adapter = mAdapter
        binding.boardPublicizeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        mAdapter.initBoardList(list)

//        mAdapter.setMyItemClickListener(object : BoardPublicizeRVAdapter.MyItemClickListener {
//            override fun onItemClick(boardIdx: String) {
//                val intent = Intent(activity, 게시물 상세보기 액티비티::class.java)
//                intent.putExtra("boardIdx", boardIdx)
//                startActivity(intent)
//            }
//        })
    }

    override fun onGetListSuccess(result: ArrayList<GetBoardListResult>) {
        Log.d("GET BOARD LIST / SUCCESS", result.toString())
        connectAdapter(result)
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}