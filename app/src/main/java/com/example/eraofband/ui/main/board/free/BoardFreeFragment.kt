package com.example.eraofband.ui.main.board.free

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.databinding.FragmentBoardFreeBinding
import com.example.eraofband.remote.board.getBoardList.GetBoardListResult
import com.example.eraofband.remote.board.getBoardList.GetBoardListService
import com.example.eraofband.remote.board.getBoardList.GetBoardListView
import com.example.eraofband.ui.main.board.info.BoardPostActivity
import kotlinx.coroutines.launch

class BoardFreeFragment : Fragment(), GetBoardListView {
    private var _binding: FragmentBoardFreeBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private lateinit var mAdapter: BoardFreeRVAdapter
    private val service = GetBoardListService()

    private var lastIdx: Int = 0

    private var add = false
    private var loading = true

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
        service.setBoardListView(this)
        getBoardIdx(0)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        layoutRefresh()
    }

    private fun getBoardIdx(boardIdx: Int) {
        lifecycleScope.launch {
            service.getBoardList(0, boardIdx)
        }
    }

    private fun connectAdapter(list: ArrayList<GetBoardListResult>) {
        mAdapter = BoardFreeRVAdapter()
        binding.boardFreeRv.adapter = mAdapter
        binding.boardFreeRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.boardFreeRv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (binding.boardFreeRv.canScrollVertically(1)) {  // 맨 위
                    Log.d("SCROLL", "TOP")
                }
                else if (binding.boardFreeRv.canScrollVertically(-1)) {  // 맨 아래
                    Log.d("SCROLL", "BOTTOM")
                    Log.d("SCROLL / SUCCESS", "${mAdapter.itemCount}")

                    if(mAdapter.itemCount % 20 == 0) {
                        if(!loading) {
                            add = true
                            loading = true

                            getBoardIdx(lastIdx)
                            Log.d("TIME", System.currentTimeMillis().toString())
                        }
                    }
                }
                else {
                    Log.d("SCROLL", "IDLE")
                }
            }
        })

        mAdapter.setMyItemClickListener(object : BoardFreeRVAdapter.MyItemClickListener {
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

    private fun layoutRefresh() {
        binding.boardFreeRl.setOnRefreshListener {
            add = false
            getBoardIdx(0)
            
            binding.boardFreeRl.isRefreshing = false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onGetListSuccess(result: ArrayList<GetBoardListResult>) {
        Log.d("GET BOARD LIST / SUCCESS", result.toString())

        if(result.isEmpty()) return

        if (!add) connectAdapter(result)
        else mAdapter.initBoardList(result)

        loading = false
    }

    override fun onGetListFailure(code: Int, message: String) {
        Log.d("GET BOARD LIST / Failure", "$code $message")
    }
}