package fr.qg.menu.common.models

import org.bukkit.event.inventory.InventoryCloseEvent

data class OpenedMenuData(
    val menu: QGMenu,
    val onClose: (InventoryCloseEvent) -> Unit,
    var information: Any?
)