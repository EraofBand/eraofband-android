package com.example.eraofband.ui.main.block

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.ActivityUserBlockBinding
import com.example.eraofband.remote.block.getBlock.GetBlockResult
import com.example.eraofband.remote.block.getBlock.GetBlockService
import com.example.eraofband.remote.block.getBlock.GetBlockView
import kotlinx.coroutines.launch

class UserBlockActivity: AppCompatActivity(), GetBlockView {
    private lateinit var binding: ActivityUserBlockBinding

    private val userList = arrayListOf<GetBlockResult>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUserBlockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.userBlockBackIv.setOnClickListener { finish() }

        val getBlockService = GetBlockService()
        getBlockService.setBlockView(this)

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

        val blockRVAdapter = BlockRVAdapter(userList, this)
        binding.userBlockRv.adapter = blockRVAdapter
        binding.userBlockRv.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

    }

    override fun onBlockSuccess(result: List<GetBlockResult>) {
        Log.d("GET/SUC", "$result")

        initRV(result)
    }

    override fun onBlockFailure(code: Int, message: String) {
        Log.d("GET/SUC", "$code $message")
    }
}