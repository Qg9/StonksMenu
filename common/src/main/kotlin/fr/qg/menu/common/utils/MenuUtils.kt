package fr.qg.menu.common.utils

import fr.qg.menu.common.models.QGMenu
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack

fun QGMenu.mapSlot(char: Char, slot: Int) : Int =
    this.pattern.subList(0, slot).count { it == char }

fun QGMenu.countSlot(char: Char) : Int =
    this.pattern.count { it == char }

fun QGMenu.putInInventory(char: Char, inventory: Inventory, items: List<ItemStack>) : Boolean {
    val map = this.pattern.withIndex().filter { it.value == char }.map { it.index }

    map.onEachIndexed { ind, slot ->
        if (ind >= items.size) return false
        inventory.setItem( slot, items[ind])
    }

    return true
}

fun QGMenu.putInInventory(char: Char, inventory: Inventory, item: ItemStack) : Boolean
    = this.putInInventory(char, inventory, listOf(item))

fun Boolean.toIntMultiplier() = if(this) 1 else -1