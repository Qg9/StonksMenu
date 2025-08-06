package fr.qg.menu.common

import fr.qg.menu.common.items.ConfigurationItem
import org.bukkit.inventory.ItemStack


interface VersionAdapter {

    companion object {
        lateinit var version: VersionAdapter
    }

    fun render(configItem: ConfigurationItem, transfor: (String) -> String) : ItemStack
}