package com.example.template

import com.example.template.app.Android
import de.robv.android.xposed.callbacks.XC_LoadPackage
import ice.easy.xposed.EasyXposedInit
import ice.easy.xposed.base.AppRegister

class XposedEntry: EasyXposedInit() {

    override val registeredApp: List<AppRegister> = listOf(
        Android()
    )

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam?) {
        super.handleLoadPackage(lpparam)
    }

}