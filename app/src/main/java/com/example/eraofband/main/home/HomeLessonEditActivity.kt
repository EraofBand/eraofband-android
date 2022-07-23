package com.example.eraofband.main.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eraofband.databinding.ActivityHomeLessonEditBinding

class HomeLessonEditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeLessonEditBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeLessonEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.homeLessonEditBackIb.setOnClickListener { finish() }
    }
}