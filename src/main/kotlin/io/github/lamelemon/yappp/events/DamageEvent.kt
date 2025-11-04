package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.Yappp.Companion.pvpDisabled
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class DamageEvent : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun entityDamageEvent(event: EntityDamageEvent) {
        val attacker = event.damageSource.causingEntity
        val victim = event.entity

        if (attacker !is Player || victim !is Player) return

        if (pvpDisabled(attacker) || pvpDisabled(victim)) {
            attacker.sendRichMessage("<red>Pvp is off for one of you!")
            victim.sendRichMessage("<red>Pvp is off for one of you!")
            event.isCancelled = true
        } else {
            TODO("Add timer")
        }
    }
}