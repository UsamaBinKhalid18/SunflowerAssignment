package com.example.sunflowerassignment

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class DataSource {
    companion object{
        fun loadFlowers(flowersText:String):List<Flower>{
            val listType: Type = object : TypeToken<ArrayList<Flower?>?>() {}.type
            return Gson().fromJson(flowersText, listType)
        }
    }
}