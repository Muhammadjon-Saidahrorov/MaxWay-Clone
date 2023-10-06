package uz.gita.foodmn.data.source.local

import android.content.Context
import uz.gita.foodmn.app.App

class MyPref {
    private val pref = App.context.getSharedPreferences("FoodDelivery",Context.MODE_PRIVATE)
    private val edit = pref.edit()

    fun getFirst():Boolean{
       return pref.getBoolean("first",true)
    }

    fun saveFirst(bool:Boolean){
        edit.putBoolean("first",bool).apply()
    }

    fun saveLastCategory(name:String){
        edit.putString("category",name).apply()
    }

    fun getLastCategory():String{
        return pref.getString("category","")!!
    }
}