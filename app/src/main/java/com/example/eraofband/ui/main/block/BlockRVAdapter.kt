package com.example.eraofband.ui.main.block

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.R
import com.example.eraofband.databinding.ItemBlockBinding
import com.example.eraofband.remote.block.getBlock.GetBlockResult


class BlockRVAdapter(private val blockList: ArrayList<GetBlockResult>, private val context: Context) : RecyclerView.Adapter<BlockRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        // 클릭 이벤트
        fun onBlock(position: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemBlockBinding = ItemBlockBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(blockList[position])

        // 차단 해제하기
        holder.binding.blockDecisionTv.setOnClickListener{ mItemClickListener.onBlock(position) }

    }
    override fun getItemCount(): Int = blockList.size

    inner class ViewHolder(val binding: ItemBlockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GetBlockResult) {
            Glide.with(context).load(user.profileImgUrl).apply(RequestOptions.centerCropTransform()).into(binding.blockProfileIv)  // 밴드 이미지
            binding.blockProfileIv.clipToOutline = true  // 모서리 깎기

            binding.blockNicknameTv.text = user.nickName

            if(user.blockChecked == 1) {  // 차단 상태인 경우
                binding.blockDecisionTv.setChipBackgroundColorResource(R.color.background_gray)
                binding.blockDecisionTv.text = "차단 해제"
            }
            else {  // 차단 해제한 경우
                binding.blockDecisionTv.setChipBackgroundColorResource(R.color.blue)
                binding.blockDecisionTv.text = "차단하기"
            }
        }
    }
}