package io.github.lamelemon.yappp.utils.timers

import io.github.lamelemon.yappp.Yappp.Companion.instance
import io.github.lamelemon.yappp.commands.PvpToggle
import io.github.lamelemon.yappp.utils.Utils.disablePvp
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import org.bukkit.Bukkit
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.PlayerDeathEvent
import org.bukkit.event.player.PlayerMoveEvent
import org.bukkit.scheduler.BukkitRunnable

class PvpToggleTimer(var duration : Long, val player: Player, val pvpToggle: PvpToggle) : BukkitRunnable(), Listener {

    init {
        messagePlayer(player, "<red>Disabling</red> PvP in...")
        Bukkit.getPluginManager().registerEvents(this, instance)
        this.runTaskTimer(instance, 0L, 20L)
    }

    override fun run() {
        if (duration <= 0) {
            disablePvp(player, true)
            this.cancel()
            return
        }

        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BIT)
        messagePlayer(player, "$duration")
        duration--
    }

    override fun cancel() {
        HandlerList.unregisterAll(this)
        pvpToggle.toggleTimers.remove(player.uniqueId)
        super.cancel()
    }

    fun cancelTimer(reason: String) {
        if (!reason.isEmpty()) {
            messagePlayer(player, "<red>Toggle cancelled! You $reason!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
        }
        this.cancel()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun playerMoveEvent(event: PlayerMoveEvent) {
        if (event.player == this.player && !event.isCancelled) cancelTimer("moved")
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun entityDamageEvent(event: EntityDamageEvent) {
        if (event.isCancelled) return

        val attacker = event.damageSource.causingEntity
        val victim = event.entity

        if (attacker == victim) return

        if (victim == this.player && attacker is Player) {
            cancelTimer("were attacked by " + attacker.name)
        } else if (attacker == this.player && victim is Player) {
            cancelTimer("attacked " + victim.name)
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun playerDeath(event: PlayerDeathEvent) {
        if (event.isCancelled) return

        if (event.player == this.player) cancelTimer("died")
    }
}