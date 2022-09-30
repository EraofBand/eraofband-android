package com.example.eraofband.ui.main.block

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemBlockBinding
import com.example.eraofband.remote.block.getBlock.GetBlockResult


class BlockRVAdapter(private val blockList: ArrayList<GetBlockResult>, private val context: Context) : RecyclerView.Adapter<BlockRVAdapter.ViewHolder>() {

    interface MyItemClickListener {
        // 클릭 이벤트
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

    }
    override fun getItemCount(): Int = blockList.size

    inner class ViewHolder(val binding: ItemBlockBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: GetBlockResult) {
            Glide.with(context).load(user.profileImgUrl).apply(RequestOptions.centerCropTransform()).into(binding.blockProfileIv)  // 밴드 이미지
            binding.blockProfileIv.clipToOutline = true  // 모서리 깎기

            binding.blockNicknameTv.text = user.nickName
        }
    }
}