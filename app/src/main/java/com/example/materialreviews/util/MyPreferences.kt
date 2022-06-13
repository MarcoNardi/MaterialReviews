package com.example.materialreviews.util

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class MyPreferences(context: Context) {

        val PREFERENCE_NAME = "SharedPreferenceExample"
        val PREFERENCE_ID = "SharedPreferenceExample"
        val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
        val FIRST_ACCESS = "FirstAccess"

        fun isFirstAcces(): Int{
            return preference.getInt(FIRST_ACCESS,1)
        }

        fun setAccess(completed: Int) {
            val editor = preference.edit()
            editor.putInt(FIRST_ACCESS, completed)
            editor.apply()
        }



    fun getId(): Int {
        return preference.getInt(PREFERENCE_ID, 1)
    }

    fun setId(id: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_ID, id)
        editor.apply()
    }


}