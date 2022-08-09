package com.example.eraofband.ui.main.search.user

import com.example.eraofband.remote.search.getUser.GetSearchUserResult

interface SearchUserInterface {
    fun initUserRV(result: List<GetSearchUserResult>)
}