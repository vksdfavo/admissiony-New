package com.student.Compass_Abroad.Utils

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.student.Compass_Abroad.modal.checkUserModel.PublicUserInfo
import androidx.core.content.edit

class SharedPrefs(context: Context) {
    private val mPreferences: SharedPreferences = context.getSharedPreferences("Sepik", Context.MODE_PRIVATE)
    private val prefsEditor: SharedPreferences.Editor = mPreferences.edit()
    private val gson: Gson = Gson()

    fun <T> getModel(key: String?, type: Class<T>): T? {
        val json = mPreferences.getString(key, null)
        return gson.fromJson(json, type)
    }

    fun saveUserList(key: String, userList: List<PublicUserInfo>) {
        val json = gson.toJson(userList)
        mPreferences.edit().putString(key, json).apply()
    }

    fun getUserList(key: String): MutableList<PublicUserInfo>? {
        val json = mPreferences.getString(key, null)
        return if (json.isNullOrEmpty()) {
            mutableListOf()
        } else {
            gson.fromJson(json, object : TypeToken<MutableList<PublicUserInfo>>() {}.type)
        }
    }


    fun saveLabelValue(key:String?, label: String, value: String) {
        // Combine label and value into a single string
        val labelValuePair = "$label,$value"

        // Store the combined string in SharedPreferences
        mPreferences.edit().putString(key, labelValuePair).apply()
    }

    fun saveModel(key: String?, model: Any?) {
        val json = gson.toJson(model)
        prefsEditor.putString(key, json).apply()
    }

    fun saveString(key: String?, value: String?) {
        prefsEditor.putString(key, value).apply()
    }

    fun getString(key: String?, defaultValue: String?): String? {
        return mPreferences.getString(key, defaultValue)
    }

    fun putString11(key: String, value: String) {
        val editor = mPreferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getString11(key: String, defaultValue: String = ""): String {
        return mPreferences.getString(key, defaultValue) ?: defaultValue
    }
    fun putStringList(key: String, list: List<String>) {
        val editor = mPreferences.edit()
        editor.putString(key, list.joinToString(","))
        editor.apply()
    }

    fun getStringList(key: String): List<String>? {
        val storedString = mPreferences.getString(key, null)
        return storedString?.split(",")?.map { it.trim() }
    }
    fun clearStringList(key: String) {
        val editor = mPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    // Clear all preferences
    fun clearString(key: String) {
        val editor = mPreferences.edit()
        editor.remove(key)
        editor.apply()
    }

    fun clearKey(key: String?) {
        if (key != null) {
            prefsEditor.remove(key).apply()
        }
    }

    fun clearPreferences() {
        prefsEditor.clear().apply()
    }

    // Retrieve label and value from SharedPreferences
    fun getLabelValue(key:String?): Pair<String, String>? {
        // Get the stored string from SharedPreferences
        val savedData = mPreferences.getString(key, null)

        // If saved data is not null, split the string and return the label and value
        if (savedData != null) {
            val parts = savedData.split(",") // Split the saved string by comma
            if (parts.size == 2) {
                val label = parts[0]  // First part is the label
                val value = parts[1]  // Second part is the value
                return Pair(label, value) // Return the label and value as a Pair
            }
        }
        return null // Return null if no data is found or if the format is incorrect
    }

    fun clearKeyLabelValue(key: String?) {
        key?.let {
            mPreferences.edit() { remove(key) }  // Apply changes asynchronously
        }
    }

    companion object {
        const val PREF_NIGHT_MODE = "night_mode"

        fun getInt(context: Context, key: String?, defaultValue: Int): Int {
            val preferences = context.getSharedPreferences("wa_data", Context.MODE_PRIVATE)
            return preferences.getInt(key, defaultValue)
        }

        fun setInt(context: Context, key: String?, value: Int) {
            val preferences = context.getSharedPreferences("wa_data", Context.MODE_PRIVATE)
            preferences.edit() { putInt(key, value) }
        }

        fun clearPrefs(context: Context) {
            val preferences = context.getSharedPreferences("wa_data", Context.MODE_PRIVATE)
            preferences.edit() { clear() }
        }

        fun getAppNightDayMode(context: Context): Int {
            val preferences = context.getSharedPreferences("wa_data", Context.MODE_PRIVATE)
            return preferences.getInt(PREF_NIGHT_MODE, AppCompatDelegate.MODE_NIGHT_NO)
        }

        fun setLang(context: Context, value: String?) {
            val preferences = context.getSharedPreferences("wa_data", Context.MODE_PRIVATE)
            preferences.edit() { putString("language", value) }
        }

        fun getLang(context: Context): String? {
            val preferences = context.getSharedPreferences("wa_data", Context.MODE_PRIVATE)
            return preferences.getString("language", "en")
        }
    }
}