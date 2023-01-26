package com.example.eraofband.ui.main.home.session.band.session

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.SessionList
import com.example.eraofband.databinding.ItemSessionListBinding

class BandRecruitSessionListRVAdapter(private val bandName: String, private val bandIdx: Int) : RecyclerView.Adapter<BandRecruitSessionListRVAdapter.ViewHolder>() {
    private var sessionList = arrayListOf<SessionList>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun showApplyPopup(bandIdx: Int, session: Int)
        fun shareRecruitSession(bandIdx: Int, session: String, sessionComment: String)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initSessionList(sessionList : List<SessionList>) {
        this.sessionList.addAll(sessionList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addSession(session : SessionList) {
        this.sessionList.add(session)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun modifySession(position: Int, session : SessionList) {
        this.sessionList[position] = session
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun deleteSession(position: Int) {
        this.sessionList.removeAt(position)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSessionListBinding = ItemSessionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sessionList[position])

        Log.d("POSITION", position.toString())

        // 클릭 이벤트
        holder.binding.sessionListVolunteerTv.setOnClickListener { mItemClickListener.showApplyPopup(bandIdx, sessionList[position].sessionInt) }
        holder.binding.sessionListShareTv.setOnClickListener { mItemClickListener.shareRecruitSession(bandIdx, sessionList[position].session, sessionList[position].sessionComment) }

    }
    override fun getItemCount(): Int = sessionList.size

    inner class ViewHolder(val binding: ItemSessionListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(session: SessionList) {
            binding.sessionListSessionTv.text = session.session

            binding.sessionListTitleTv.text = "$bandName ${session.session} 모집"

            binding.sessionListIntroTv.text = session.sessionComment
            binding.sessionListCntTv.text = "모집인원 ${session.count}"
        }
    }
}