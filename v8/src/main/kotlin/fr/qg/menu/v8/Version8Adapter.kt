package fr.qg.menu.v8

import fr.qg.menu.common.VersionAdapter
import fr.qg.menu.common.items.ConfigurationItem
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class Version8Adapter : VersionAdapter {

    override fun render(citem: ConfigurationItem, transfo: (String) -> String): ItemStack {
        val item = ItemStack(citem.type, 1)
        val meta = item.itemMeta ?: return item

        citem.name?.let {
            val parsed = transfo.invoke(it)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', parsed))
        }

        if (citem.lore.isNotEmpty()) {
            val parsedLore = citem.lore.map {
                val parsed = transfo.invoke(it)
                ChatColor.translateAlternateColorCodes('&', parsed)
            }
            meta.setLore(parsedLore)
        }

        if (citem.enchanted) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        return item
    }
}