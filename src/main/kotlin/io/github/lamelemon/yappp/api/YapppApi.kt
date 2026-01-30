package io.github.lamelemon.yappp.api

import org.bukkit.entity.Player

interface YapppApi {
    fun inCombat(player: Player): Boolean
}