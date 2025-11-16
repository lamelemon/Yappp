package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.Yappp.Companion.instance
import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils
import io.github.lamelemon.yappp.utils.timers.PvpToggleTimer
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

class PvpToggle(val toggleCooldown: Long, val disableTimer: Long) : BasicCommand {
    private val cooldowns = mutableMapOf<UUID, Long>()
    val toggleTimers = mutableMapOf<UUID, PvpToggleTimer>()

    override fun execute(
        commandSourceStack: CommandSourceStack,
        args: Array<out String>
    ) {
        val player = commandSourceStack.sender
        if (player !is Player) return

        val uuid = player.uniqueId

        if (CombatManager.inCombat(player)){
            Utils.messagePlayer(player, "<red>In Combat!</red>")
            Utils.simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        val lastUsed = cooldowns[uuid]
        // Check if command is on cooldown for player
        if (lastUsed is Long) {
            Utils.messagePlayer(player, "<red>Command on cooldown for " + (toggleCooldown - (commandSourceStack.location.world.gameTime - lastUsed) / 20) + " more seconds!</red>")
            Utils.simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        if (!Utils.pvpEnabled(player)) {
            cooldowns[uuid] = commandSourceStack.location.world.gameTime
            Utils.enablePvp(player)
            // Avoid extremely slow memory leak
            instance.server.scheduler.runTaskLater(instance, Runnable{
                cooldowns.remove(uuid)
            }, toggleCooldown * 20)
            return
        }

        if (disableTimer <= 0) {
            Utils.disablePvp(player)
            return
        }

        if (!toggleTimers.containsKey(uuid)) {
            toggleTimers[uuid] = PvpToggleTimer(disableTimer, player, this)
        }
    }
}