package io.github.lamelemon.yappp.utils.timers

import io.github.lamelemon.yappp.commands.PvpToggleCommand
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

// Serves as the cooldown between toggling pvp on and off for a specific player
class ToggleCooldown(var cooldown: Long, val player: Player, var pvpToggleCommand: PvpToggleCommand) : BukkitRunnable() {

    override fun run() {
        cooldown--
        if (cooldown <= 0) { // End timer
            pvpToggleCommand.removeCooldown(player.uniqueId)
            this.cancel()
        }
    }
}