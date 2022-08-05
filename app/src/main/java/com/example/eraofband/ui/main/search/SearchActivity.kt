package com.example.eraofband.ui.main.search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.eraofband.databinding.ActivitySearchBinding
import com.google.android.material.tabs.TabLayoutMediator

class SearchActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySearchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchBackIb.setOnClickListener {
            finish()
        }

        val searchVPAdapter = SearchVPAdapter(this)
        binding.searchVp.adapter = searchVPAdapter

        TabLayoutMediator(binding.searchTb, binding.searchVp) { tab, position ->
            when (position) {
                0 -> tab.text = "유저"
                1 -> tab.text = "밴드"
                else -> tab.text = "레슨"
            }
        }.attach()
    }
}