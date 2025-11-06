package io.github.lamelemon.yappp.utils

import io.github.lamelemon.yappp.utils.PvpUtils.instance
import io.github.lamelemon.yappp.utils.PvpUtils.messagePlayer
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.UUID

class CombatManager(val combatDuration: Long) {
    private val combatTimers = mutableMapOf<UUID, BukkitTask>()

    fun tagPlayer(player: Player) {
        if (combatDuration.toInt() != 0) {
            combatTimers[player.uniqueId]?.cancel()
            messagePlayer(player, "<red>Entered combat!</red>")

            combatTimers[player.uniqueId] = Bukkit.getScheduler().runTaskLater(instance, Runnable {
                combatTimers.remove(player.uniqueId)
                messagePlayer(player, "<green>Exited combat!</green>")
            }, combatDuration)
        }
    }

    fun isInCombat(player: Player): Boolean {
        return combatTimers.containsKey(player.uniqueId)
    }
}