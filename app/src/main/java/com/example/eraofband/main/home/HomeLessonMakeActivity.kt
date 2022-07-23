package com.example.eraofband.main.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eraofband.databinding.ActivityHomeLessonMakeBinding

class HomeLessonMakeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeLessonMakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeLessonMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeLessonMakeBackIb.setOnClickListener { finish() }
    }
}