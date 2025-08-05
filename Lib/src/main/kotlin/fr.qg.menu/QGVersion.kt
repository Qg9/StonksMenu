package fr.qg.menu

import fr.qg.menu.common.VersionAdapter

enum class QGVersion(val code: String) {
    V1_8("fr.qg.menu.v8.Version8Adapter"),
    V1_21("fr.qg.menu.v21.Version21Adapter");

    fun load() : VersionAdapter  {
        val clazz = Class.forName(code)
        val instance = clazz.getDeclaredConstructor().newInstance()
        return instance as VersionAdapter
    }
}