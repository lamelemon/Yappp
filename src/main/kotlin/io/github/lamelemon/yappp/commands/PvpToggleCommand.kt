package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import io.github.lamelemon.yappp.utils.Utils.pvpDisabled
import io.github.lamelemon.yappp.utils.Utils.togglePvp
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.UUID

class PvpToggleCommand(val toggleCooldown: Long, val disableTimer: Long) : BasicCommand {
    private val cooldowns2 = mutableMapOf<UUID, Long>()

    // TODO: Add optional argument for operators/people with a permission to toggle pvp for another player
    //  (horrible waste of time just use /execute)
    // TODO: Add optional delay between running command and then disabling pvp
    //  (use a player event to detect if they're disabling pvp when they do something?)
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
        val lastUsed = cooldowns2[uuid]

        if (lastUsed is Long && toggleCooldown > currentTime - lastUsed) { // Command is currently on cooldown for player
            messagePlayer(player, "<red>Command on cooldown for " + (toggleCooldown - (currentTime - lastUsed)) + " more seconds!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        if (pvpDisabled(player)) {
            messagePlayer(player, "PvP <green>Enabled!</green>")
            cooldowns2[uuid] = currentTime
        } else {
            messagePlayer(player, "PvP <red>Disabled!</red>")
        }

        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
        togglePvp(player)
    }
}