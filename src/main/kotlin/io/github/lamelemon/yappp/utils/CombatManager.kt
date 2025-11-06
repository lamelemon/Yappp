package io.github.lamelemon.yappp.utils

import io.github.lamelemon.yappp.utils.Utils.instance
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.timers.CombatTimer
import org.bukkit.entity.Player
import java.util.UUID

class CombatManager(val defaultDuration: Long) {
    private val combatTimers = mutableMapOf<UUID, CombatTimer>()

    fun tagPlayer(player: Player) {
        if (defaultDuration.toInt() == 0) return

        if (!inCombat(player)) {
            messagePlayer(player, "<red>Entered combat!</red>")
            combatTimers[player.uniqueId] = CombatTimer(defaultDuration, player)
            combatTimers[player.uniqueId]?.runTaskTimer(instance, 0, 20)
        } else {
            combatTimers[player.uniqueId]?.combatDuration = defaultDuration
        }
    }

    fun inCombat(player: Player): Boolean {
        return combatTimers.containsKey(player.uniqueId)
    }

    fun removeTimer(uuid: UUID) {
        combatTimers.remove(uuid)
    }
}