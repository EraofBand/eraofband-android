package com.example.eraofband.ui.main.home.notice

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemNoticeBinding
import com.example.eraofband.remote.notice.getNotice.GetNoticeResult
import com.example.eraofband.remote.user.getMyPage.GetUserPofol
import com.example.eraofband.ui.main.home.lesson.LessonInfoActivity
import com.example.eraofband.ui.main.home.lessonlike.HomeLessonLikeRVAdapter

class NoticeRVAdapter(list: ArrayList<GetNoticeResult>) : RecyclerView.Adapter<NoticeRVAdapter.ViewHolder>() {
    private var noticeList = list

    @SuppressLint("NotifyDataSetChanged")
    fun reset() {
        if (noticeList.isNotEmpty()) {
            noticeList.clear()
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNoticeBinding = ItemNoticeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(noticeList[position])
    }

    override fun getItemCount(): Int = noticeList.size

    inner class ViewHolder(val binding: ItemNoticeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(notice: GetNoticeResult) {

            Glide.with(itemView).load(notice.noticeImg)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.itemNoticeProfileIv) // 레슨 이미지

            binding.itemNoticeTitleTv.text = notice.noticeHead // 알림 타이틀
            binding.itemNoticeIntroTv.text = notice.noticeBody // 알림 설명
            binding.itemNoticeDateTv.text = notice.updatedAt // 알림 시간

            if (notice.status == "ACTIVE") // 새로운 알림이면 파란색 동그라미
                binding.itemNoticeNewIv.visibility = View.VISIBLE
            else
                binding.itemNoticeNewIv.visibility = View.INVISIBLE
        }
    }

}