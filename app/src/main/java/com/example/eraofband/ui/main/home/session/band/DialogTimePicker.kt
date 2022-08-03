package com.example.eraofband.ui.main.home.session.band

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.eraofband.databinding.DialogTimepickerBinding

class DialogTimePicker (private val selectedTime : String) : DialogFragment() {

    private lateinit var binding : DialogTimepickerBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DialogTimepickerBinding.inflate(inflater, container, false)

        val time = selectedTime  // 넘겨받은 날짜를 기본으로

        // 날짜 저장
        binding.dialogTimepicker.setOnTimeChangedListener { timePicker, hour, minute -> // string으로 넣어주기 편하게 미리 변환
            var am_pm = "AM"
            var selectedHour = hour.toString()
            var selectedMinute = minute.toString()
            if(hour > 12) {
                am_pm = "PM"
                selectedHour = (hour - 12).toString()
            }
            if(minute < 10){
                selectedMinute = "0$minute"
            }

            mItemClickListener.saveShow("$am_pm $selectedHour:$selectedMinute")
        }

        binding.dialogTimeNextTv.setOnClickListener {
            dismiss()
        }

        return binding.root
    }

    interface MyItemClickListener {
        fun saveShow(time : String)
    }

    private lateinit var mItemClickListener: MyItemClickListener
    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        mItemClickListener = itemClickListener
    }

}