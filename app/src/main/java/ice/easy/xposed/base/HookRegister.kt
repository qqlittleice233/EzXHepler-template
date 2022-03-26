package ice.easy.xposed.base

abstract class HookRegister {
    var isInit: Boolean = false
    abstract fun init()
}