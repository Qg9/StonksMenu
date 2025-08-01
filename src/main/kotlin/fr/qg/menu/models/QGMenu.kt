package fr.qg.menu.models

import fr.qg.menu.actions.ClickScript

data class QGMenu(
    val title: String,
    val pattern: List<Char>,
    val items: Map<Char, MenuItem>,
    val scripts: Map<Char, ClickScript>
) {
    val size: Int
        get() = pattern.size
}