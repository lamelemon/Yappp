package io.github.lamelemon.yappp.timers

import io.github.lamelemon.yappp.Yappp.Companion.instance
import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.disablePvp
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.sendActionBar
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.scheduler.BukkitRunnable

class CombatTag(var combatDuration: Long, val player: Player) : BukkitRunnable(), Listener {

    init {
        Bukkit.getPluginManager().registerEvents(this, instance)
    }

    override fun run() {
        if (combatDuration <= 0) { // End timer
            this.cancel()
            return
        }
        combatDuration--
        sendActionBar(player, "<red>In combat for " + combatDuration + "s!</red>")
    }

    override fun cancel() {
        messagePlayer(player, "<green>Exited combat!</green>")
        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
        CombatManager.combatTimers.remove(player.uniqueId)
        HandlerList.unregisterAll(this)
        super.cancel()
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun playerDeath(event: PlayerDeathEvent) {
        if (!event.isCancelled) this.cancel()
    }
}