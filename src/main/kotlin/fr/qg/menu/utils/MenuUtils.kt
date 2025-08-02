package fr.qg.menu.utils

import fr.qg.menu.models.QGMenu

fun QGMenu.mapSlot(char: Char, slot: Int) : Int =
    this.pattern.subList(0, slot).count { it == char }