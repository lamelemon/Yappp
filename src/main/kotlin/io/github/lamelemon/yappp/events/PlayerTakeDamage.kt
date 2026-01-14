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

class PlayerTakeDamage(val selfPvp: Boolean) : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun playerDamageEvent(event: EntityDamageEvent) {
        val victim = event.entity
        val attacker = event.damageSource.causingEntity

        // Verify that it's a pvp situation and prevent people from being immune to self damage (if config says to)
        if (attacker !is Player || victim !is Player ||  (attacker.uniqueId == victim.uniqueId && selfPvp)) return

        when {
            // Attacker's pvp is disabled
            !pvpEnabled(attacker) -> {
                messagePlayer(attacker, "<red>Your PvP is currently disabled!")
                simplePlaySound(attacker, Sound.BLOCK_NOTE_BLOCK_BASS)
                event.isCancelled = true
            }

            // Victim's pvp is disabled
            !pvpEnabled(victim) -> {
                messagePlayer(attacker, "<red>" + victim.name + " has their PvP currently disabled!")
                simplePlaySound(attacker, Sound.BLOCK_NOTE_BLOCK_BASS)
                event.isCancelled = true
            }

            // Combat tag both parties if parties aren't the same player
            attacker.uniqueId != victim.uniqueId -> {
                if (event.finalDamage >= victim.health) return
                CombatManager.combatTag(listOf(victim, attacker))
            }
        }
    }
}