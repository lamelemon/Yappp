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
        CombatManager.combatTimers.remove(event.player.uniqueId)

        if (disablePvp) {
            disablePvp(event.player, false)
        }

        if (event.damageSource.causingEntity is Player && keepInventory) {
            event.keepInventory = keepInventory
            event.drops.clear()
        }
    }
}