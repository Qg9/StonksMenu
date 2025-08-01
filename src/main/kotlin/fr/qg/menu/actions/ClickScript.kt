package fr.qg.menu.actions

import fr.qg.menu.MenuAPI.open
import fr.qg.menu.models.QGMenu
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player

interface ClickScript {
    fun action(menu: QGMenu, player: Player, slot: Int)
}

class ScriptNode(val requirement: List<MenuRequirement>,
                      val needed: Int,
                      val valid: ClickScript,
                      val invalid: ClickScript) : ClickScript {
    override fun action(menu: QGMenu, player: Player, slot: Int) {
        val accepted = requirement.filter { it.test(player) }.size
        if(accepted > needed && needed > -1) valid.action(menu, player, slot) else invalid.action(menu, player, slot)
    }
}

class ScriptAction(val actions: List<String>) : ClickScript {
    override fun action(menu: QGMenu, player: Player, slot: Int) {
        actions.forEach { line ->
            val parts = line.trim().split(" ", limit = 2)
            val key = parts[0].lowercase()
            val argument = parts.getOrNull(1)?.replace("%player%", player.name)

            when (key) {
                "[close]" -> player.closeInventory()

                "[update]" -> {
                    player.closeInventory()
                    menu.open(player)
                }

                "[player]" -> {
                    if (argument != null) {
                        player.performCommand(argument)
                    }
                }

                "[console]" -> {
                    if (argument != null) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), argument)
                    }
                }

                "[sound]" -> {
                    if (argument != null) {
                        try {
                            val sound = Sound.valueOf(argument.uppercase())
                            player.playSound(player.location, sound, 1f, 1f)
                        } catch (e: IllegalArgumentException) {
                            player.sendMessage("§cSon inconnu: $argument")
                        }
                    }
                }

                "[message]" -> {
                    if (argument != null) {
                        player.sendMessage(argument.replace("&", "§"))
                    }
                }

                else -> {
                    player.sendMessage("§cAction inconnue: $line")
                }
            }
        }
    }
}
