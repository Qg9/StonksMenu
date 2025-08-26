package fr.qg.menu.common

import fr.qg.menu.common.actions.ClickScript
import fr.qg.menu.common.actions.EvalExRequirement
import fr.qg.menu.common.actions.MenuRequirement
import fr.qg.menu.common.actions.NotRequirement
import fr.qg.menu.common.actions.PermissionRequirement
import fr.qg.menu.common.actions.ScriptAction
import fr.qg.menu.common.actions.ScriptNode
import fr.qg.menu.common.items.ConfigurationItem
import fr.qg.menu.common.items.MenuItem
import fr.qg.menu.common.models.OpenedMenuData
import fr.qg.menu.common.models.QGMenu
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCloseEvent
import org.bukkit.inventory.Inventory
import org.bukkit.plugin.java.JavaPlugin
import java.util.*
import kotlin.text.Typography.section

object MenuRegistry {
    val openMenus: MutableMap<UUID, OpenedMenuData> = mutableMapOf()

    internal fun handleClose(event: InventoryCloseEvent) {
        val player = event.player as? Player ?: return
        openMenus.remove(player.uniqueId)?.onClose?.invoke(event)
    }

    internal fun handleClick(event: InventoryClickEvent) {
        val player = event.whoClicked as? Player ?: return
        val menu = openMenus[player.uniqueId] ?: return
        if (event.view.title != menu.menu.title) return

        event.isCancelled = true
        val slot = event.slot
        menu.onClick(player, slot, event)
    }

}

fun QGMenu.open(player: Player, gens: (Inventory) -> Unit = {},
                close: (InventoryCloseEvent) -> Unit = {},
                data: Any? = null) {
    val inventory = this.generateInventory(player)
    MenuRegistry.openMenus[player.uniqueId] = OpenedMenuData(this, close, data)
    gens.invoke(inventory)
    player.openInventory(inventory)
}

private fun QGMenu.generateInventory(player: Player): Inventory {
    val inv = Bukkit.createInventory(null, size, title)

    pattern.forEachIndexed { slot, char ->
        val item = items[char]?.render(player) ?: return@forEachIndexed
        inv.setItem(slot, item)
    }

    return inv
}

private fun OpenedMenuData.onClick(player: Player, slot: Int, event: InventoryClickEvent) {
    val script = this.menu.scripts[this.menu.pattern[slot]] ?: return
    script.action(this, player, slot, event)
}

fun ConfigurationSection.asMenuItem(): MenuItem {

    val type = Material.matchMaterial((this.getString("type") ?: "").uppercase())
        ?: error("Invalid material type for item '${this.currentPath}'")

    val data = this.getInt("data", 0)
    val amount = this.getInt("amount", 1)
    val name = this.getString("name")
    val lore = this.getStringList("lore")
    val enchanted = this.getBoolean("enchanted", false)

    return ConfigurationItem(
        type = type,
        amount = amount,
        data = data,
        name = name,
        lore = lore,
        enchanted = enchanted
    )
}

fun ConfigurationSection.asMenu(): QGMenu {
    val title = this.getString("title", "")!!.replace("&", "§")

    val pattern = this.getStringList("pattern").joinToString("").replace(" ", "").toList()
    if (pattern.size > 54 || pattern.size % 9 != 0) error("Menu size must be multiple of 9 (from 9 to 54)")
    val itemsSection = this.getConfigurationSection("items")

    val items = mutableMapOf<Char, MenuItem>()

    if (itemsSection != null)
        for (key in itemsSection.getKeys(false)) {
            itemsSection.getConfigurationSection(key)?.let {
                items[key.first()] = it.asMenuItem()
            }
        }

    val scripts = mutableMapOf<Char, ClickScript>()
    val scriptsSection = this.getConfigurationSection("scripts")
    if (scriptsSection != null) {
        for (key in scriptsSection.getKeys(false)) {
            val symbol = key.first()
            val scriptData = scriptsSection.getConfigurationSection(key)
            if (scriptData != null) {
                scripts[symbol] = scriptData.loadClickScript()
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

 internal fun ConfigurationSection.loadClickScript(): ClickScript =
     when {
            this.contains("actions") -> {
                val actions = this.getStringList("actions")
                ScriptAction(actions)
            }

            this.contains("requirement")
                    && this.contains("valid")
                    && this.contains("invalid") -> {

                val requirements = this.getStringList("requirement").loadRequirements()
                val needed = this.getInt("needed", -1)

                val validSection = this.getConfigurationSection("valid")
                    ?: error("Missing 'valid' section for ScriptNode in ${this.name}")
                val invalidSection = this.getConfigurationSection("invalid")
                    ?: error("Missing 'invalid' section for ScriptNode in ${this.name}")

                val valid = validSection.loadClickScript()
                val invalid = invalidSection.loadClickScript()

                ScriptNode(requirements, needed, valid, invalid)
            }

            else -> error("Invalid ClickScript definition in section '${this.name}'")
     }

internal fun List<String>.loadRequirements(): List<MenuRequirement> =
    this.mapNotNull { line ->
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