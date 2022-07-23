package com.example.eraofband.main.home.band

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.R
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemNewBandBinding


class HomeNewBandRVAdapter(private var newBandList : ArrayList<Band>) : RecyclerView.Adapter<HomeNewBandRVAdapter .ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemNewBandBinding = ItemNewBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newBandList[position])
    }
    override fun getItemCount(): Int = newBandList.size

    inner class ViewHolder(val binding: ItemNewBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: Band) {
            binding.itemNewBandProfileIv.setImageResource(0)
            binding.itemNewBandLocationTv.text = "강남구"
            binding.itemNewBandTitleTv.text = "밴드"
            binding.itemNewBandMemberCountTv.text = "4/5"
        }
    }
}