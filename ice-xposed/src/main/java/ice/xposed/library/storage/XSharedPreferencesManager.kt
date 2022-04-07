package ice.xposed.library.storage

import android.annotation.TargetApi
import android.content.SharedPreferences
import android.os.Build
import de.robv.android.xposed.XSharedPreferences
import ice.xposed.library.util.Warning

@TargetApi(Build.VERSION_CODES.O_MR1)
@Warning("For LSPosed, test is needed for others")
object XSharedPreferencesManager {

    private val sharedPreferenceProxyMap = hashMapOf<Int, XSharedPreferenceProxy>()

    @JvmStatic
    @Synchronized
    fun getXSharedPreferenceProxy(packageName: String, prefFileName: String): XSharedPreferenceProxy? {
        val hash = (packageName + prefFileName).hashCode()
        return if (sharedPreferenceProxyMap.containsKey(hash)) {
            sharedPreferenceProxyMap[hash]
        } else {
            val pref = XSharedPreferences(packageName, prefFileName)
            if (pref.file.canRead()) {
                val proxy = XSharedPreferenceProxy(pref)
                sharedPreferenceProxyMap[hash] = proxy
                proxy
            } else {
                null
            }
        }
    }

    class XSharedPreferenceProxy(private val sharedPreference: SharedPreferences) {

        private fun <T> checkReadable(block: (canRead: Boolean) -> T): T {
            return block((sharedPreference as XSharedPreferences).file.canRead())
        }

        fun canRead(): Boolean {
            return checkReadable { it }
        }

        fun getString(key: String, defaultValue: String?): XSharedPreferenceResult<String?> {
            return checkReadable { canRead ->
                XSharedPreferenceResult(canRead, if (canRead) sharedPreference.getString(key, defaultValue) else null)
            }
        }

        fun getInt(key: String, defaultValue: Int): XSharedPreferenceResult<Int> {
            return checkReadable { canRead ->
                XSharedPreferenceResult(canRead, if (canRead) sharedPreference.getInt(key, defaultValue) else defaultValue)
            }
        }

        fun getLong(key: String, defaultValue: Long): XSharedPreferenceResult<Long> {
            return checkReadable { canRead ->
                XSharedPreferenceResult(canRead, if (canRead) sharedPreference.getLong(key, defaultValue) else defaultValue)
            }
        }

        fun getFloat(key: String, defaultValue: Float): XSharedPreferenceResult<Float> {
            return checkReadable { canRead ->
                XSharedPreferenceResult(canRead, if (canRead) sharedPreference.getFloat(key, defaultValue) else defaultValue)
            }
        }

        fun getBoolean(key: String, defaultValue: Boolean = false): XSharedPreferenceResult<Boolean> {
            return checkReadable { canRead ->
                XSharedPreferenceResult(canRead, if (canRead) sharedPreference.getBoolean(key, defaultValue) else defaultValue)
            }
        }

        @Warning("Only for LSPosed")
        fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            sharedPreference.registerOnSharedPreferenceChangeListener(listener)
        }

        @Warning("Only for LSPosed")
        fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
            sharedPreference.unregisterOnSharedPreferenceChangeListener(listener)
        }

        data class XSharedPreferenceResult<T> (val isSuccess: Boolean, val value: T)
    }

}