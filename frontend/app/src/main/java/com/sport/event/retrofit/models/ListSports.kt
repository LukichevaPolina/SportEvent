package com.sport.event.retrofit.models

class ListSports (
    val sports: ArrayList<Sport>
) {

    fun getArraySports(): ArrayList<String> {
        val arraySport = ArrayList<String>()
        for (sport in sports) {
            arraySport.add(sport.sport)
        }
        return arraySport
    }
}