package fr.qg.menu.common.listeners

import fr.qg.menu.common.MenuRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent

object MenuListener : Listener {

    @EventHandler
    fun onClick(event: InventoryClickEvent) {
        if(event.slot < 0)return
        MenuRegistry.handleClick(event)
    }

    @EventHandler
    fun onClose(event: InventoryCloseEvent) {
        MenuRegistry.handleClose(event)
    }
}
