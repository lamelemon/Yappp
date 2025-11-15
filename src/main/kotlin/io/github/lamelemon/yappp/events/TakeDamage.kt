package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.pvpDisabled
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

        // Verify that it's a pvp situation and prevent people from tanking self damage
        if (attacker !is Player || victim !is Player ||  attacker.uniqueId == victim.uniqueId && selfPvp) return

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
            attacker.uniqueId != victim.uniqueId -> { // Tag both players as being in combat unless its self damage
                CombatManager.tagPlayer(victim)
                CombatManager.tagPlayer(attacker)
            }
        }

    }
}