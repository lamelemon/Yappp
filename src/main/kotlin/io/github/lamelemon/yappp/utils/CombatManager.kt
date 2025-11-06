package io.github.lamelemon.yappp.utils

import io.github.lamelemon.yappp.utils.PvpUtils.instance
import io.github.lamelemon.yappp.utils.PvpUtils.messagePlayer
import io.github.lamelemon.yappp.utils.PvpUtils.sendActionBar
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class CombatManager(val combatDuration: Long) {
    private val combatTimers = mutableMapOf<UUID, PvpTimer>()

    fun tagPlayer(player: Player) {
        if (combatDuration.toInt() == 0) return

        if (!combatTimers.containsKey(player.uniqueId)) {
            messagePlayer(player, "<red>Entered combat!</red>")
            combatTimers[player.uniqueId] = PvpTimer(combatDuration, player)
            combatTimers[player.uniqueId]?.runTaskTimer(instance, 0, 20)
        } else {
            combatTimers[player.uniqueId]?.duration = combatDuration
        }
    }

    fun isInCombat(player: Player): Boolean {
        return combatTimers.containsKey(player.uniqueId)
    }

    fun removeTimer(uuid: UUID) {
        combatTimers.remove(uuid)
    }
}