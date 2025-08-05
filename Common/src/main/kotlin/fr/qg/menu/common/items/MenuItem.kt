package fr.qg.menu.common.items

import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

interface MenuItem {
    fun render(player: Player?, transfo: (String) -> String = { it }) : ItemStack
}