package fr.qg.menu.models

import org.bukkit.Material

data class MenuItem (
    val type: Material,
    val data: Int = 0,
    val name: String? = null,
    val lore: List<String> = emptyList(),
    val enchanted: Boolean = false
)