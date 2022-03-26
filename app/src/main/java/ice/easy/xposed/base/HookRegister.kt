package ice.easy.xposed.base

import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class HookRegister {
    lateinit var lpparam: XC_LoadPackage.LoadPackageParam
    var isInit: Boolean = false
    abstract fun init()
}