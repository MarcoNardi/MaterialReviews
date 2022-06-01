package com.example.materialreviews

import android.content.Context

class MyPreferences(context: Context) {

    val PREFERENCE_NAME = "SharedPreferenceExample"
    val PREFERENCE_ID = "SharedPreferenceExample"
    val preference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

    fun getId(): Int {
        return preference.getInt(PREFERENCE_ID, 1)
    }

    fun setId(id: Int) {
        val editor = preference.edit()
        editor.putInt(PREFERENCE_ID, id)
        editor.apply()
    }
}