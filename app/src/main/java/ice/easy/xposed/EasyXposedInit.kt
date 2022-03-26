package ice.easy.xposed

import com.github.kyuubiran.ezxhelper.init.EzXHelperInit
import de.robv.android.xposed.IXposedHookInitPackageResources
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.callbacks.XC_InitPackageResources
import de.robv.android.xposed.callbacks.XC_LoadPackage
import ice.easy.xposed.base.AppRegister

abstract class EasyXposedInit: IXposedHookLoadPackage, IXposedHookZygoteInit, IXposedHookInitPackageResources {

    private lateinit var packageParam: XC_LoadPackage.LoadPackageParam
    abstract val registeredApp: List<AppRegister>

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        packageParam = lpparam!!
        registeredApp.forEach { app ->
            if (app.packageName == lpparam.packageName && (lpparam.processName in app.processName || app.processName.isEmpty())) {
                EzXHelperInit.initHandleLoadPackage(lpparam)
                EzXHelperInit.setLogTag(app.logTag)
                EzXHelperInit.setToastTag(app.logTag)
                app.handleLoadPackage(lpparam)
            }
        }
    }

    override fun handleInitPackageResources(resparam: XC_InitPackageResources.InitPackageResourcesParam?) {
        if (this::packageParam.isInitialized) {
            registeredApp.forEach { app ->
                if (app.packageName == packageParam.packageName && (packageParam.processName in app.processName || app.processName.isEmpty())) {
                    app.handleInitPackageResources(resparam!!)
                }
            }
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam?) {
        EzXHelperInit.initZygote(startupParam!!)
    }

}