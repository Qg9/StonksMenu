package fr.qg.menu

import fr.qg.menu.common.VersionAdapter
import fr.qg.menu.common.listeners.MenuListener
import fr.qg.menu.v21.Version21Adapter
import org.bukkit.Bukkit
import org.bukkit.plugin.java.JavaPlugin

object MenuAPI {

    fun register(plugin: JavaPlugin)  {
        plugin.server.pluginManager.registerEvents(MenuListener, plugin)
        val v1_8 = Bukkit.getBukkitVersion().startsWith("1.8")

        VersionAdapter.version = (if (v1_8) QGVersion.V1_8 else QGVersion.V1_21).load()
    }
}