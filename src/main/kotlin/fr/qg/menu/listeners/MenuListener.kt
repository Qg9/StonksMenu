package fr.qg.menu.listeners

import fr.qg.menu.MenuAPI
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

object MenuListener : Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        MenuAPI.handleClick(event)
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        MenuAPI.handleClose(event)
    }
}
