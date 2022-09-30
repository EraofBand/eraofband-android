package com.example.eraofband.ui.main.block

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.ActivityUserBlockBinding
import com.example.eraofband.remote.block.block.BlockService
import com.example.eraofband.remote.block.block.BlockView
import com.example.eraofband.remote.block.cancelBlock.CancelBlockService
import com.example.eraofband.remote.block.cancelBlock.CancelBlockView
import com.example.eraofband.remote.block.getBlock.GetBlockResult
import com.example.eraofband.remote.block.getBlock.GetBlockService
import com.example.eraofband.remote.block.getBlock.GetBlockView
import kotlinx.coroutines.launch

class UserBlockActivity: AppCompatActivity(), GetBlockView, CancelBlockView, BlockView {
    private lateinit var binding: ActivityUserBlockBinding

    private lateinit var blockRVAdapter: BlockRVAdapter
    private val userList = arrayListOf<GetBlockResult>()

    private val cancelBlockService = CancelBlockService()
    private val blockService = BlockService()
    private var blockPosition = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userBlockBackIv.setOnClickListener { finish() }

        val getBlockService = GetBlockService()
        getBlockService.setBlockView(this)

        cancelBlockService.setBlockView(this)
        blockService.setBlockView(this)

        lifecycleScope.launch {
            getBlockService.getBlockList(getJwt()!!)
        }
    }

    private fun getJwt() : String? {
        val userSP = getSharedPreferences("user", MODE_PRIVATE)
        return userSP.getString("jwt", "")
    }

    private fun initRV(item: List<GetBlockResult>) {
        userList.addAll(item)

        blockRVAdapter = BlockRVAdapter(userList, this)
        binding.userBlockRv.adapter = blockRVAdapter
        binding.userBlockRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        blockRVAdapter.setMyItemClickListener(object : BlockRVAdapter.MyItemClickListener {
            override fun onBlock(position: Int) {
                if(userList[position].blockChecked == 1) {
                    cancelBlockService.cancelBlock(getJwt()!!, userList[position].userIdx)
                    blockPosition = position
                } else {
                    blockService.block(getJwt()!!, userList[position].userIdx)
                    blockPosition = position
                }

            }

        })
    }

    override fun onGetSuccess(result: List<GetBlockResult>) {
        Log.d("GET/SUC", "$result")

        initRV(result)
    }

    override fun onGetFailure(code: Int, message: String) {
        Log.d("GET/SUC", "$code $message")
    }

    override fun onBlockSuccess(result: String) {
        Log.d("BLOCK/SUC", result)

        userList[blockPosition].blockChecked = 1
        blockRVAdapter.notifyItemChanged(blockPosition)

        blockPosition = -1
    }

    override fun onBlockFailure(code: Int, message: String) {
        Log.d("BLOCK/SUC", "$code $message")
    }

    override fun onCancelSuccess(result: String) {
        Log.d("CANCEL/SUC", result)

        userList[blockPosition].blockChecked = 0
        blockRVAdapter.notifyItemChanged(blockPosition)

        blockPosition = -1
    }

    override fun onCancelFailure(code: Int, message: String) {
        Log.d("CANCEL/SUC", "$code $message")
    }
}