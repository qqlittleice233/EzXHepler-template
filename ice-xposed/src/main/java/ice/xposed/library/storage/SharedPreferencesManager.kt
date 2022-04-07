package ice.xposed.library.storage

import android.content.Context
import android.content.SharedPreferences
import org.json.JSONObject

object SharedPreferencesManager {

    private val sharedPreferenceProxyMap = mutableMapOf<String, SharedPreferenceProxy>()

    @JvmStatic
    @Synchronized
    fun getSharedPreferenceProxy(context: Context, spName: String): SharedPreferenceProxy {
        return sharedPreferenceProxyMap.getOrPut(spName) { SharedPreferenceProxy(context, spName) }
    }

    class SharedPreferenceProxy(context: Context, spName: String) {

        val sharedPreference: SharedPreferences = context.getSharedPreferences(spName, Context.MODE_PRIVATE)
        private val mEditor by lazy { sharedPreference.edit() }

        val sharedPreferenceBackup by lazy { SharedPreferenceBackup() }

        fun put(key: String, value: Any) {
            when (value) {
                is String -> mEditor.putString(key, value)
                is Int -> mEditor.putInt(key, value)
                is Long -> mEditor.putLong(key, value)
                is Float -> mEditor.putFloat(key, value)
                is Boolean -> mEditor.putBoolean(key, value)
            }
            mEditor.apply()
        }

        fun getString(key: String, defaultValue: String?): String? {
            return sharedPreference.getString(key, defaultValue)
        }

        fun getInt(key: String, defaultValue: Int): Int {
            return sharedPreference.getInt(key, defaultValue)
        }

        fun getLong(key: String, defaultValue: Long): Long {
            return sharedPreference.getLong(key, defaultValue)
        }

        fun getFloat(key: String, defaultValue: Float): Float {
            return sharedPreference.getFloat(key, defaultValue)
        }

        fun getBoolean(key: String, defaultValue: Boolean = false): Boolean {
            return sharedPreference.getBoolean(key, defaultValue)
        }

        fun remove(key: String) {
            mEditor.remove(key)
            mEditor.apply()
        }

        fun clear() {
            mEditor.clear()
            mEditor.apply()
        }

        inner class SharedPreferenceBackup {

            var separator = ";separator;"

            fun getSPJson(): String {
                val json = JSONObject()
                val map = mutableMapOf<String, String>()
                for ((key, value) in sharedPreference.all) {
                    when (value) {
                        is String -> { map[key] = "string${separator}${value}" }
                        is Int -> { map[key] = "int${separator}${value}" }
                        is Long -> { map[key] = "long${separator}${value}" }
                        is Float -> { map[key] = "float${separator}${value}" }
                        is Boolean -> { map[key] = "boolean${separator}${value}" }
                    }
                }
                val data = JSONObject(map.toMap())
                json.put("data", data)
                return json.toString()
            }

            fun saveSPJSON(jsonData: String): Boolean {
                try {
                    val json = JSONObject(jsonData)
                    val data = json.getJSONObject("data")
                    data.keys().forEach { key ->
                        val rawValue = data.getString(key)
                        val type = rawValue.split(separator)[0]
                        val value = rawValue.split(separator)[1]
                        when (type) {
                            "string" -> { put(key, value) }
                            "int" -> { put(key, value.toInt()) }
                            "long" -> { put(key, value.toLong()) }
                            "float" -> { put(key, value.toFloat()) }
                            "boolean" -> { put(key, value.toBoolean()) }
                        }
                    }
                    return true
                } catch (e: Exception) { return false }
            }

        }

    }
}