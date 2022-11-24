package com.example.eraofband.ui.main.search.band

import com.example.eraofband.remote.search.getBand.GetSearchBandResult

interface SearchBandInterface {
    fun initBandRV(result: List<GetSearchBandResult>)
}