package io.github.lamelemon.yappp.utils

import io.github.lamelemon.yappp.Yappp.Companion.instance
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.timers.CombatTag
import org.bukkit.entity.Player
import java.util.UUID

object CombatManager {
    val combatTimers = mutableMapOf<UUID, CombatTag>()
    private var tagDuration: Long = 0

    fun combatTag(player: Player) {
        if (tagDuration.toInt() == 0) return

        if (!inCombat(player)) {
            messagePlayer(player, "<red>Entered combat!</red>")
            combatTimers[player.uniqueId] = CombatTag(tagDuration, player)
            combatTimers[player.uniqueId]?.runTaskTimer(instance, 0, 20)
        } else {
            combatTimers[player.uniqueId]?.combatDuration = tagDuration
        }
    }

    fun combatTag(players: Iterable<Player>) {
        if (tagDuration.toInt() == 0) return

        for (player in players) {
            if (!inCombat(player)) {
                messagePlayer(player, "<red>Entered combat!</red>")
                combatTimers[player.uniqueId] = CombatTag(tagDuration, player)
                combatTimers[player.uniqueId]?.runTaskTimer(instance, 0, 20)
            } else {
                combatTimers[player.uniqueId]?.combatDuration = tagDuration
            }
        }
    }

    fun inCombat(player: Player): Boolean {
        return combatTimers.containsKey(player.uniqueId)
    }

    fun setTagDuration(duration: Long) {
        tagDuration = duration
    }
}