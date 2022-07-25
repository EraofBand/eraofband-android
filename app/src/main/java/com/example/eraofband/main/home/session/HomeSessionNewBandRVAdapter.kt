package com.example.eraofband.main.home.session

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.eraofband.data.Band
import com.example.eraofband.databinding.ItemSessionNewBandBinding


class HomeSessionNewBandRVAdapter(private var newBandList : ArrayList<Band>) : RecyclerView.Adapter<HomeSessionNewBandRVAdapter .ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemSessionNewBandBinding = ItemSessionNewBandBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(newBandList[position])
    }
    override fun getItemCount(): Int = newBandList.size

    inner class ViewHolder(val binding: ItemSessionNewBandBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(band: Band) {
            binding.itemNewBandProfileIv.setImageResource(0)
            binding.itemNewBandLocationTv.text = "강남구"
            binding.itemNewBandTitleTv.text = "밴드"
            binding.itemNewBandMemberCountTv.text = "4/5"
        }
    }
}