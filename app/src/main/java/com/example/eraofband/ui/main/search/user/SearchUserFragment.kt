package com.example.eraofband.ui.main.search.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.eraofband.databinding.FragmentSearchUserBinding
import com.example.eraofband.remote.search.getUser.GetSearchUserResult
import com.example.eraofband.ui.main.search.SearchActivity

class SearchUserFragment(): Fragment() {

    private var _binding: FragmentSearchUserBinding? = null
    private val binding get() = _binding!! // 바인딩 누수 방지

    private var searchUser = arrayListOf<GetSearchUserResult>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchUserBinding.inflate(inflater, container, false)

        searchUser = getUser() as ArrayList<GetSearchUserResult>
        initRVAdapter(searchUser)
        Log.d("SEARCH USER", searchUser.toString())

        return binding.root
    }

    private fun getUser(): List<GetSearchUserResult> {
        return (activity as SearchActivity).userLists
    }

    fun initRVAdapter(userList: List<GetSearchUserResult>){
        val searchUserRVAdapter = SearchUserRVAdapter()
        binding.searchUserRv.adapter = searchUserRVAdapter
        binding.searchUserRv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        searchUserRVAdapter.initUserList(userList)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}