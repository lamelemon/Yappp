package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.pvpDisabled
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class DamageEvent() : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun entityDamageEvent(event: EntityDamageEvent) {
        val victim = event.entity
        val attacker = event.damageSource.causingEntity

        if (attacker !is Player || victim !is Player) return

        when {
            pvpDisabled(attacker) -> {
                messagePlayer(attacker, "<red>Your PvP is currently off!")
                event.isCancelled = true
            }
            pvpDisabled(victim) -> {
                messagePlayer(attacker, "<red>PvP is off for that player!")
                event.isCancelled = true
            }
            else -> {
                combatManager.tagPlayer(victim)
                combatManager.tagPlayer(attacker)
            }
        }

    }
}