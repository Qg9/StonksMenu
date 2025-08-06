package fr.qg.menu.common.utils

import fr.qg.menu.common.models.QGMenu

fun QGMenu.mapSlot(char: Char, slot: Int) : Int =
    this.pattern.subList(0, slot).count { it == char }


fun QGMenu.countSlot(char: Char) : Int =
    this.pattern.count { it == char }

fun Boolean.toIntMultiplier() = if(this) 1 else -1