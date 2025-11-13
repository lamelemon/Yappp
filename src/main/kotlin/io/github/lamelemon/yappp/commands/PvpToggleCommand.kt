package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.disablePvp
import io.github.lamelemon.yappp.utils.Utils.enablePvp
import io.github.lamelemon.yappp.utils.Utils.instance
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.pvpDisabled
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import io.github.lamelemon.yappp.utils.Utils.togglePvp
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

class PvpToggleCommand(val toggleCooldown: Long, val disableTimer: Long) : BasicCommand {
    private val cooldowns = mutableMapOf<UUID, Long>()

    override fun execute(
        commandSourceStack: CommandSourceStack,
        args: Array<out String>
    ) {
        val player = commandSourceStack.sender
        if (player !is Player) return

        val uuid = player.uniqueId

        if (combatManager.inCombat(player)){
            messagePlayer(player, "<red>In Combat!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        val currentTime = commandSourceStack.location.world.gameTime
        val lastUsed = cooldowns[uuid]

        // Check if command is on cooldown for player
        // Works because if cooldowns doesn't contain lastUsed it doesn't return a proper Long
        if (lastUsed is Long) {
            messagePlayer(player, "<red>Command on cooldown for " + (toggleCooldown - (currentTime - lastUsed)) + " more seconds!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        val scheduler = instance.server.scheduler
        if (pvpDisabled(player)) {
            cooldowns[uuid] = currentTime
            enablePvp(player)
            // Avoid extremely slow memory leak
            scheduler.runTaskLater(instance, Runnable{
                cooldowns.remove(uuid)
            }, toggleCooldown * 20)
        } else {
            if (disableTimer > 0) {
                scheduler.runTaskLater(instance, Runnable{
                    disablePvp(player)
                }, disableTimer * 20)
            } else {
                disablePvp(player)
            }
        }

        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
    }
}