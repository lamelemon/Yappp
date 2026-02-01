package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.disablePvp
import org.bukkit.damage.DamageType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent


class PlayerDeath(var keepInventory: Boolean, var disablePvp: Boolean): Listener{

    @EventHandler(priority = EventPriority.HIGHEST)
    fun playerDeath(event: PlayerDeathEvent) {
        if (event.isCancelled) return

        val player = event.player
        val inCombat = CombatManager.inCombat(player) // Take player out of combat
        CombatManager.combatTimers.remove(player.uniqueId)

        // Disable player's pvp if config says to
        if (disablePvp) {
            disablePvp(event.player, false)
        }

        if (!keepInventory) return

        // Keep inventory on player death
        if (event.damageSource.causingEntity is Player || (inCombat && event.damageSource.damageType == DamageType.ON_FIRE)) {
            event.keepInventory = true
            event.keepLevel = true
            event.drops.clear()
            event.setShouldDropExperience(false)
        }
    }
}