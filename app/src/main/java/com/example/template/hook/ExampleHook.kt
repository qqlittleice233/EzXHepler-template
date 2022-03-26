package com.example.template.hook

import com.github.kyuubiran.ezxhelper.utils.*
import ice.easy.xposed.base.HookRegister

// Example hook
object ExampleHook : HookRegister() {
    override fun init() {
        // Example for findMethod
        findMethod("android.widget.Toast") {
            name == "show"
        }.hookBefore {
            Log.i("Hooked before Toast.show()")
        }

        // Example for getMethodByDesc
        getMethodByDesc("Landroid/widget/Toast;->show()V").hookAfter {
            Log.i("Hooked after Toast.show()")
        }
    }
}