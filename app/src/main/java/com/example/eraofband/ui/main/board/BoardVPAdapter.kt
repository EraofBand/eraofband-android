package com.example.eraofband.ui.main.board

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.eraofband.ui.main.board.free.BoardFreeFragment
import com.example.eraofband.ui.main.board.my.BoardMyFragment
import com.example.eraofband.ui.main.board.publicize.BoardPublicizeFragment
import com.example.eraofband.ui.main.board.question.BoardQuestionFragment
import com.example.eraofband.ui.main.board.trade.BoardTradeFragment

class BoardVPAdapter (fragment : Fragment) : FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 5

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> BoardFreeFragment()
            1 -> BoardQuestionFragment()
            2 -> BoardPublicizeFragment()
            3 -> BoardTradeFragment()
            else -> BoardMyFragment()
        }
    }
}