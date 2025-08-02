package fr.qg.menu.models

import fr.qg.menu.actions.ClickScript

data class QGMenu(
    val title: String,
    val pattern: List<Char>,
    val items: MutableMap<Char, MenuItem>,
    val scripts: MutableMap<Char, ClickScript>
) {
    val size: Int
        get() = pattern.size
}