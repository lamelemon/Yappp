package io.github.lamelemon.yappp.internal

import io.github.lamelemon.yappp.api.YapppApi
import io.github.lamelemon.yappp.utils.CombatManager
import org.bukkit.entity.Player

class APImpl : YapppApi {
    override fun inCombat(player: Player): Boolean {
        return CombatManager.inCombat(player)
    }
}