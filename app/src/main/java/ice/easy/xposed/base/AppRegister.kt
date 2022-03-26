package ice.easy.xposed.base

import com.github.kyuubiran.ezxhelper.utils.Log
import com.github.kyuubiran.ezxhelper.utils.Log.logexIfThrow
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage

abstract class AppRegister: IXposedHookLoadPackage, IXposedHookInitPackageResources {

    abstract val packageName: String
    abstract val processName: List<String>
    abstract val logTag: String

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {}
    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam) {}

    protected fun autoInitHooks(vararg hook: HookRegister) {
        hook.forEach {
            runCatching {
                if (it.isInit) return@forEach
                it.init()
                it.isInit = true
                Log.i("Inited hook: ${it.javaClass.simpleName}")
            }.logexIfThrow("Failed init hook: ${it.javaClass.simpleName}")
        }
    }

}