package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.disablePvp
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent


class PlayerDeath(var keepInventory: Boolean, var disablePvp: Boolean): Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    fun playerDeath(event: PlayerDeathEvent) {
        if (event.isCancelled) return

        CombatManager.combatTimers.remove(event.player.uniqueId) // Take player out of combat

        // Disable player's pvp if config says to
        if (disablePvp) {
            disablePvp(event.player, false)
        }

        if (!keepInventory) return

        // Keep inventory on player death
        if (event.damageSource.causingEntity is Player) {
            event.keepInventory = true
            event.drops.clear()
            event.setShouldDropExperience(false)
            event.keepLevel = true
        }
    }
}