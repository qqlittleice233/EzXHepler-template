package com.example.template.app

import com.example.template.hook.ExampleHook
import de.robv.android.xposed.callbacks.XC_LoadPackage
import ice.easy.xposed.base.AppRegister

class Android: AppRegister() {

    override val logTag: String = "Test"
    override val packageName: String = "android"
    override val processName: List<String> = emptyList()

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        super.handleLoadPackage(lpparam)
        autoInitHooks(
            ExampleHook
        )
    }


}