package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.pvpDisabled
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent

class DamageEvent(val selfPvp: Boolean) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun entityDamageEvent(event: EntityDamageEvent) {
        val victim = event.entity
        val attacker = event.damageSource.causingEntity

        // Verify that it's a pvp situation and prevent people from tanking self damage
        // && in parentheses for clarity
        // (please use parentheses even if it's not required it doesn't hurt performance)
        if (attacker !is Player || victim !is Player || (attacker.uniqueId == victim.uniqueId && !selfPvp)) return

        when {
            pvpDisabled(attacker) -> {
                messagePlayer(attacker, "<red>Your PvP is currently off!")
                simplePlaySound(attacker, Sound.BLOCK_NOTE_BLOCK_BASS)
                event.isCancelled = true
            }
            pvpDisabled(victim) -> {
                messagePlayer(attacker, "<red>PvP is off for that player!")
                simplePlaySound(attacker, Sound.BLOCK_NOTE_BLOCK_BASS)
                event.isCancelled = true
            }
            else -> { // Tag both players as being in combat
                combatManager.tagPlayer(victim)
                combatManager.tagPlayer(attacker)
            }
        }

    }
}