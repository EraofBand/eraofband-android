package com.example.eraofband.ui.main.home.lesson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.eraofband.databinding.ItemStudentBinding
import com.example.eraofband.remote.lesson.getLessonInfo.LessonMembers

class LessonStudentRVAdapter : RecyclerView.Adapter<LessonStudentRVAdapter.ViewHolder>() {
    private var studentList = arrayListOf<LessonMembers>()

    interface MyItemClickListener {
        // 클릭 이벤트
        fun userInfo(userIdx: Int)
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initStudentList(studentList : List<LessonMembers>) {
        this.studentList.addAll(studentList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStudentBinding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studentList[position])

        holder.binding.studentProfileIv.setOnClickListener { // 수강생 프사 클릭 시
            mItemClickListener.userInfo(studentList[position].userIdx)
        }
        holder.binding.studentInfoLy.setOnClickListener { // 수강생 닉네임 클릭 시
            mItemClickListener.userInfo(studentList[position].userIdx)
        }
    }
    override fun getItemCount(): Int = studentList.size

    inner class ViewHolder(val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: LessonMembers) {

            Glide.with(itemView)  // 수강생 프사
                .load(student.profileImgUrl)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.circleCropTransform())
                .into(binding.studentProfileIv)

            binding.studentNicknameTv.text = student.nickName  // 수강생 닉네임
            binding.studentIntroTv.text = student.introduction  // 수강생 한 줄 소개
            binding.studentSessionTv.text = when(student.mySession) {  // 레슨 종목
                0 -> "보컬"
                1 -> "기타"
                2 -> "베이스"
                3 -> "키보드"
                else -> "드럼"
            }
        }
    }
}