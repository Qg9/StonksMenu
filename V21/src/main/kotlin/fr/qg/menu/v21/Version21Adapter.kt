package fr.qg.menu.v21

import fr.qg.menu.common.VersionAdapter
import fr.qg.menu.common.items.ConfigurationItem
import org.bukkit.ChatColor
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack

class Version21Adapter : VersionAdapter {

    override fun render(configItem: ConfigurationItem, transfor: (String) -> String): ItemStack {
        val item = ItemStack(configItem.type, 1)
        val meta = item.itemMeta ?: return item

        configItem.name?.let {
            val parsed = transfor.invoke(it)
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', parsed))
        }

        if (configItem.lore.isNotEmpty()) {
            val parsedLore = configItem.lore.map {
                val parsed = transfor.invoke(it)
                ChatColor.translateAlternateColorCodes('&', parsed)
            }
            meta.setLore(parsedLore)
        }

        if (configItem.enchanted) {
            meta.addEnchant(Enchantment.UNBREAKING, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        meta.setCustomModelData(configItem.data)

        item.itemMeta = meta
        return item
    }
}