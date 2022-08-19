package com.example.eraofband.ui.main.board.question

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.databinding.FragmentBoardQuestionBinding
import com.example.eraofband.remote.board.getBoardList.GetBoardListResult
import com.example.eraofband.remote.board.getBoardList.GetBoardListService
import com.example.eraofband.remote.board.getBoardList.GetBoardListView
import com.example.eraofband.ui.main.board.info.BoardPostActivity

class BoardQuestionFragment : Fragment(), GetBoardListView {
    private var _binding: FragmentBoardQuestionBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지
    private lateinit var mAdapter: BoardQuestionRVAdapter
    private val service = GetBoardListService()
    private var lastIdx: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBoardQuestionBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.onResume()
        service.setBoardListView(this)
        service.getBoardList(3,0)
    }

    private fun connectAdapter(list: ArrayList<GetBoardListResult>) {
        mAdapter = BoardQuestionRVAdapter()
        binding.boardQuestionRv.adapter = mAdapter
        binding.boardQuestionRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.boardQuestionRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (binding.boardQuestionRv.canScrollVertically(1)) {  // 맨 위
                    Log.d("SCROLL", "TOP")
                }
                else if (binding.boardQuestionRv.canScrollVertically(-1)) {  // 맨 아래
                    Log.d("SCROLL", "BOTTOM")
                    Log.d("SCROLL / SUCCESS", "${mAdapter.itemCount}")

                    if(mAdapter.itemCount % 20 == 0) {
                        service.getBoardList(0, lastIdx)
                    }
                }
                else {
                    Log.d("SCROLL", "IDLE")
                }
            }
        })
        mAdapter.setMyItemClickListener(object : BoardQuestionRVAdapter.MyItemClickListener {
            override fun onItemClick(boardIdx: Int) {
                val intent = Intent(activity, BoardPostActivity::class.java)
                intent.putExtra("boardIdx", boardIdx)
                startActivity(intent)
            }
            override fun onLastIndex(boardIdx: Int) {
                lastIdx = boardIdx
            }
        })
        
        mAdapter.initBoardList(list)
    }

    override fun onGetListSuccess(result: ArrayList<GetBoardListResult>) {
        Log.d("GET BOARD LIST / SUCCESS", result.toString())
        if (lastIdx == 0)
            connectAdapter(result)
        else
            mAdapter.initBoardList(result)
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}