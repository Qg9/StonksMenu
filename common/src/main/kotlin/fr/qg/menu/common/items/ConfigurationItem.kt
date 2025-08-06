package fr.qg.menu.common.items

import fr.qg.menu.common.MenuRegistry
import fr.qg.menu.common.VersionAdapter
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class ConfigurationItem (
    val type: Material,
    val amount: Int = 1,
    val data: Int = 0,
    val name: String? = null,
    val lore: List<String> = emptyList(),
    val enchanted: Boolean = false
) : MenuItem {

    override fun render(player: Player?, transfo: (String) -> String): ItemStack =
        VersionAdapter.version.render(this){
            transfo.invoke(PlaceholderAPI.setPlaceholders(player, it))
        }
}