package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.Yappp.Companion.instance
import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.disablePvp
import io.github.lamelemon.yappp.utils.Utils.enablePvp
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.pvpDisabled
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import io.github.lamelemon.yappp.utils.timers.ToggleTimer
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.util.*

class PvpToggle(val toggleCooldown: Long, val disableTimer: Long) : BasicCommand {
    private val cooldowns = mutableMapOf<UUID, Long>()
    val toggleTimers = mutableMapOf<UUID, BukkitTask>()

    override fun execute(
        commandSourceStack: CommandSourceStack,
        args: Array<out String>
    ) {
        val player = commandSourceStack.sender
        if (player !is Player) return

        val uuid = player.uniqueId

        if (CombatManager.inCombat(player)){
            messagePlayer(player, "<red>In Combat!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        val lastUsed = cooldowns[uuid]
        // Check if command is on cooldown for player
        if (lastUsed is Long) {
            messagePlayer(player, "<red>Command on cooldown for " + (toggleCooldown - (commandSourceStack.location.world.gameTime - lastUsed) / 20) + " more seconds!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        if (pvpDisabled(player)) {
            cooldowns[uuid] = commandSourceStack.location.world.gameTime
            enablePvp(player)
            // Avoid extremely slow memory leak
            instance.server.scheduler.runTaskLater(instance, Runnable{
                cooldowns.remove(uuid)
            }, toggleCooldown * 20)
        } else {
            if (disableTimer > 0) {
                toggleTimers[uuid] = ToggleTimer(disableTimer, player, this).runTaskTimer(instance, 0, 20)
            } else {
                disablePvp(player)
            }
        }
    }
}