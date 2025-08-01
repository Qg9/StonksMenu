package fr.qg.menu

import fr.qg.menu.actions.ClickScript
import fr.qg.menu.actions.EvalExRequirement
import fr.qg.menu.actions.MenuRequirement
import fr.qg.menu.actions.NotRequirement
import fr.qg.menu.actions.PermissionRequirement
import fr.qg.menu.actions.ScriptAction
import fr.qg.menu.actions.ScriptNode
import fr.qg.menu.listeners.MenuListener
import fr.qg.menu.models.MenuItem
import fr.qg.menu.models.QGMenu
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemFlag
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import java.util.UUID

object MenuAPI {

    private val openMenus: MutableMap<UUID, QGMenu> = mutableMapOf()

    fun register(plugin: JavaPlugin) =
        plugin.server.pluginManager.registerEvents(MenuListener, plugin)

    fun QGMenu.open(player: Player) {
        val inventory = this.generateInventory()
        openMenus[player.uniqueId] = this
        player.openInventory(inventory)
    }

    fun QGMenu.open(player: Player, gens: (Inventory) -> Any) {
        val inventory = this.generateInventory()
        openMenus[player.uniqueId] = this
        gens.invoke(inventory)
        player.openInventory(inventory)
    }

    internal fun handleClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val menu = openMenus[player.uniqueId] ?: return
        if (event.view.title != menu.title) return

        event.isCancelled = true
        val slot = event.rawSlot
        val item = event.currentItem

        menu.onClick(player, slot, item)
    }

    internal fun handleClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        openMenus.remove(player.uniqueId)
    }

    private fun QGMenu.generateInventory(): Inventory {
        val inv = Bukkit.createInventory(null, size, title)

        pattern.forEachIndexed { slot, char ->
            val item = items[char]?.toItemStack() ?: return@forEachIndexed
            inv.setItem(slot, item)
        }

        return inv
    }

    private fun QGMenu.onClick(player: Player, slot: Int, item: ItemStack?) {
        this.scripts[this.pattern[slot]]?.action(this, player, slot) ?: return
    }

    fun MenuItem.toItemStack(): ItemStack {
        val item = ItemStack(type, 1)
        val meta = item.itemMeta ?: return item

        name?.let {
            meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', it))
        }

        if (lore.isNotEmpty()) {
            meta.lore = lore.map { ChatColor.translateAlternateColorCodes('&', it) }
        }

        if (enchanted) {
            meta.addEnchant(Enchantment.DURABILITY, 1, true)
            meta.addItemFlags(ItemFlag.HIDE_ENCHANTS)
        }

        item.itemMeta = meta
        item.durability = data.toShort()
        return item
    }

    fun getMenu(section: ConfigurationSection): QGMenu {
        val title = section.getString("title") ?: error("Menu must have a 'title'")

        val pattern = section.getStringList("pattern")
            .joinToString("") // concatène toutes les lignes
            .toList() // transforme la chaîne en List<Char>

        val itemsSection = section.getConfigurationSection("items")
            ?: error("Menu must have an 'items' section")

        val items = mutableMapOf<Char, MenuItem>()
        for (key in itemsSection.getKeys(false)) {
            val symbol = key.first()
            val itemData = itemsSection.getConfigurationSection(key) ?: continue

            val type = Material.matchMaterial(itemData.getString("type") ?: "")
                ?: error("Invalid material type for item '$key'")

            val data = itemData.getInt("data", 0)
            val name = itemData.getString("name")
            val lore = itemData.getStringList("lore")
            val enchanted = itemData.getBoolean("enchanted", false)

            items[symbol] = MenuItem(
                type = type,
                data = data,
                name = name,
                lore = lore,
                enchanted = enchanted
            )
        }

        val scripts = mutableMapOf<Char, ClickScript>()
        val scriptsSection = section.getConfigurationSection("scripts")
        if (scriptsSection != null) {
            for (key in scriptsSection.getKeys(false)) {
                val symbol = key.first()
                val scriptData = scriptsSection.getConfigurationSection(key)
                if (scriptData != null) {
                    scripts[symbol] = loadClickScript(scriptData)
                } else {
                    // fallback si jamais c'est une simple liste directement
                    val actions = scriptsSection.getStringList(key)
                    scripts[symbol] = ScriptAction(actions)
                }
            }
        }

        return QGMenu(
            title = title,
            pattern = pattern,
            items = items,
            scripts = scripts
        )
    }

    private fun loadClickScript(section: ConfigurationSection): ClickScript {
        return when {
            section.contains("actions") -> {
                val actions = section.getStringList("actions")
                ScriptAction(actions)
            }

            section.contains("requirement")
                    && section.contains("needed")
                    && section.contains("valid")
                    && section.contains("invalid") -> {

                val requirements = loadRequirements(section.getStringList("requirement"))
                val needed = section.getInt("needed", -1)

                val validSection = section.getConfigurationSection("valid")
                    ?: error("Missing 'valid' section for ScriptNode in ${section.name}")
                val invalidSection = section.getConfigurationSection("invalid")
                    ?: error("Missing 'invalid' section for ScriptNode in ${section.name}")

                val valid = loadClickScript(validSection)
                val invalid = loadClickScript(invalidSection)

                ScriptNode(requirements, needed, valid, invalid)
            }

            else -> error("Invalid ClickScript definition in section '${section.name}'")
        }
    }

    private fun loadRequirements(lines: List<String>): List<MenuRequirement> {
        return lines.mapNotNull { line ->
            val trimmed = line.trim()
            val isNegated = trimmed.startsWith("!")
            val body = if (isNegated) trimmed.drop(1).trim() else trimmed

            val match = Regex("""\[(\w+)]\s+(.*)""").find(body)
            if (match != null) {
                val (type, value) = match.destructured
                val base: MenuRequirement = when (type.lowercase()) {
                    "perm" -> PermissionRequirement(value)
                    "eval" -> EvalExRequirement(value)
                    else -> return@mapNotNull null
                }
                if (isNegated) NotRequirement(base) else base
            } else null
        }
    }
}