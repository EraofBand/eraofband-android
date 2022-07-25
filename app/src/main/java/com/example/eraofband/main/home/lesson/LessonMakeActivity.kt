package com.example.eraofband.main.home.lesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eraofband.databinding.ActivityLessonMakeBinding

class LessonMakeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonMakeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonMakeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeLessonMakeBackIb.setOnClickListener { finish() }
    }
}