package com.example.eraofband.main.home.session.band

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Band
import com.example.eraofband.data.SessionList
import com.example.eraofband.databinding.ItemSessionListBinding

class BandRecruitSessionListRVAdapter(private val bandName: String, private val bandIdx: Int) : RecyclerView.Adapter<BandRecruitSessionListRVAdapter.ViewHolder>() {
    private var sessionList = arrayListOf<SessionList>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun showApplyPopup(code: String, bandIdx: Int, session: Int)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSessionListBinding = ItemSessionListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(sessionList[position])

        // 클릭 이벤트
        holder.binding.sessionListVolunteerTv.setOnClickListener { mItemClickListener.showApplyPopup("apply", bandIdx, sessionList[position].sessionInt) }

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