package fr.qg.menu.common

/*
class Version8Adapter : VersionAdapter {

    override fun render(citem: ConfigurationItem, transfo: (String) -> String) : ItemStack {
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
            meta.addEnchant(Enchantment., 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        return item
    }
}*/