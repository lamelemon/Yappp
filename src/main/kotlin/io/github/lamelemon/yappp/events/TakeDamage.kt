package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.pvpEnabled
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class TakeDamage(val selfPvp: Boolean) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun entityDamageEvent(event: EntityDamageEvent) {
        val victim = event.entity
        val attacker = event.damageSource.causingEntity

        // Verify that it's a pvp situation and prevent people from being immune to self damage (if config says so)
        if (attacker !is Player || victim !is Player ||  attacker.uniqueId == victim.uniqueId && selfPvp) return

        when {
            !pvpEnabled(attacker) -> {
                messagePlayer(attacker, "<red>Your PvP is currently disabled!")
                simplePlaySound(attacker, Sound.BLOCK_NOTE_BLOCK_BASS)
                event.isCancelled = true
            }
            !pvpEnabled(victim) -> {
                messagePlayer(attacker, "<red>" + victim.name + "has their PvP currently disabled!")
                simplePlaySound(attacker, Sound.BLOCK_NOTE_BLOCK_BASS)
                event.isCancelled = true
            }
            attacker.uniqueId != victim.uniqueId -> { // Tag both players as being in combat unless its self damage
                CombatManager.combatTag(listOf(victim, attacker))
            }
        }
    }
}