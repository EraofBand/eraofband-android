package com.example.eraofband.main.home.bandlist

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandMemberBinding
import com.example.eraofband.databinding.ItemSessionListBinding
import com.example.eraofband.databinding.ItemSessionVolunteerBinding

class SessionListRVAdapter : RecyclerView.Adapter<SessionListRVAdapter.ViewHolder>() {
    private var sessionList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initSessionList(volunteerList : List<Band>) {
        this.sessionList.addAll(volunteerList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSessionListBinding = ItemSessionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sessionList[position])

        // 클릭 이벤트
    }
    override fun getItemCount(): Int = sessionList.size

    inner class ViewHolder(val binding: ItemSessionListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(session: Band) {

        }
    }
}