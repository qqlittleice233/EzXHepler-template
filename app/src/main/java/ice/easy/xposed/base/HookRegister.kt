package ice.easy.xposed.base

import com.github.kyuubiran.ezxhelper.init.InitFields
import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class HookRegister {
    private lateinit var lpparam: XC_LoadPackage.LoadPackageParam
    var isInit: Boolean = false
    abstract fun init()

    fun setLoadPackageParam(loadPackageParam: XC_LoadPackage.LoadPackageParam) {
        lpparam = loadPackageParam
    }

    protected fun getLoadPackageParam(): XC_LoadPackage.LoadPackageParam? {
        if (this::lpparam.isInitialized) {
            return lpparam
        }
        return null
    }

    protected fun getDefaultClassLoader(): ClassLoader? {
        if (this::lpparam.isInitialized) {
            return lpparam.classLoader
        }
        if (InitFields.isEzXClassLoaderInited) {
            return InitFields.ezXClassLoader
        }
        return null
    }

}