package wind.maimusic.utils

import android.content.Context
import android.content.SharedPreferences
import wind.maimusic.MaiApp

/**
 * @Description
 * android数据的存储和查询
 */
object SpUtil {
    private const val name = "sp_app_config"
    private val prefs: SharedPreferences by lazy {
        MaiApp.getInstance().applicationContext
            .getSharedPreferences(name, Context.MODE_PRIVATE)
    }

    fun saveValue(key: String, value: Any) = with(prefs.edit()) {
        when (value) {
            is Long -> putLong(key, value)
            is Int -> putInt(key, value)
            is String -> putString(key, value)
            is Float -> putFloat(key, value)
            is Boolean -> putBoolean(key, value)
            else -> throw IllegalArgumentException("SharedPreferences Wrong Type")
        }.apply()
    }

    fun saveValues(keys: List<String>, values: List<Any>) {
        val editor = prefs.edit()
        for (i in values.indices) {
            val key = keys[i]
            when (val v = values[i]) {
                is Long -> editor.putLong(key, v)
                is Int -> editor.putInt(key, v)
                is String -> editor.putString(key, v)
                is Float -> editor.putFloat(key, v)
                is Boolean -> editor.putBoolean(key, v)
                else -> throw IllegalArgumentException("SharedPreferences Wrong Type")
            }
        }
        editor.apply()
    }

    @Suppress("UNCHECKED_CAST")
    fun getValue(key: String, default: Any): Any? = with(prefs) {
        return when (default) {
            is Int -> getInt(key, default)
            is String -> this.getString(key, default)
            is Long -> getLong(key, default)
            is Float -> getFloat(key, default)
            is Boolean -> getBoolean(key, default)
            else -> throw IllegalArgumentException("SharedPreferences 类型错误")
        }
    }

    fun getString(key: String, default: String = ""): String {
        return getValue(key, default) as String
    }


    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getValue(key, default) as Boolean
    }

    fun getFloat(key: String, default: Float = 0f): Float {
        return getValue(key, default) as Float
    }

    fun getInt(key: String, default: Int = 0): Int {
        return getValue(key, default) as Int
    }

    fun getLong(key: String, default: Long = 0): Long {
        return getValue(key, default) as Long
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun remove(key: String) {
        prefs.edit().remove(key).apply()
    }
}