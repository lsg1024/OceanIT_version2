package com.example.oceanit.Retrofit

import android.content.Context
import android.content.SharedPreferences

object Loginkey {

    private val MY_ACCOUNT : String = "account"
    private val MY_TOKEN : String = "success"

    fun setUserKey(context: Context, input : Int) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("user_key", input.toString())
        editor.commit()
    }

    fun getUserKey(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        return prefs.getString("user_key", "").toString()
    }

    fun removeKey(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_ACCOUNT, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear().commit()
    }

    fun setTokenKey(context: Context, input : String) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_TOKEN, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.putString("user_token", input)
        editor.commit()
    }

    fun getTokenKey(context: Context) : String {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_TOKEN, Context.MODE_PRIVATE)
        return prefs.getString("user_token", "").toString()
    }

    fun removeTokenKey(context: Context) {
        val prefs : SharedPreferences = context.getSharedPreferences(MY_TOKEN, Context.MODE_PRIVATE)
        val editor : SharedPreferences.Editor = prefs.edit()
        editor.clear().commit()
    }

}