package com.example.eraofband.main.home.lesson

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eraofband.databinding.ActivityLessonEditBinding

class LessonEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLessonEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLessonEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeLessonEditBackIb.setOnClickListener { finish() }
    }
}