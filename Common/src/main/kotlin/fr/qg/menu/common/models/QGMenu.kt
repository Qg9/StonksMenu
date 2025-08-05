package fr.qg.menu.common.models

import fr.qg.menu.common.actions.ClickScript
import fr.qg.menu.common.items.MenuItem

data class QGMenu(
    val title: String,
    val pattern: List<Char>,
    val items: MutableMap<Char, MenuItem>,
    val scripts: MutableMap<Char, ClickScript>
) {
    val size: Int
        get() = pattern.size
}