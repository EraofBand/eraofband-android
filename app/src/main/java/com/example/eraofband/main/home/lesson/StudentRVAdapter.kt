package com.example.eraofband.main.home.lesson

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemBandListBinding
import com.example.eraofband.databinding.ItemBandMemberBinding
import com.example.eraofband.databinding.ItemStudentBinding

class StudentRVAdapter : RecyclerView.Adapter<StudentRVAdapter.ViewHolder>() {
    private var studentList = arrayListOf<Band>()

    interface MyItemClickListener {
        // 클릭 이벤트
    }

    private lateinit var mItemClickListener: MyItemClickListener

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

    @SuppressLint("NotifyDataSetChanged")
    fun initStudentList(studentList : List<Band>) {
        this.studentList.addAll(studentList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemStudentBinding = ItemStudentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(studentList[position])

        // 클릭 이벤트
    }
    override fun getItemCount(): Int = studentList.size

    inner class ViewHolder(val binding: ItemStudentBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(student: Band) {
            binding.studentProfileIv.setImageResource(R.drawable.ic_captain)

            binding.studentNicknameTv.text = "닉네임입니다"
            binding.studentIntroTv.text = "소개입니다"
        }
    }
}