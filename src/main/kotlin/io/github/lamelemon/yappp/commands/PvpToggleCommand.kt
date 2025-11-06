package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.instance
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import io.github.lamelemon.yappp.utils.Utils.pvpDisabled
import io.github.lamelemon.yappp.utils.Utils.togglePvp
import io.github.lamelemon.yappp.utils.timers.ToggleCooldown
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.UUID

class PvpToggleCommand(val toggleCooldown: Long, val disableTimer: Long) : BasicCommand {
    private val cooldowns = mutableMapOf<UUID, ToggleCooldown>()

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

        if (combatManager.inCombat(player)){
            messagePlayer(player, "<red>In Combat!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        if (cooldowns.containsKey(player.uniqueId)) { // Command is currently on cooldown for player
            messagePlayer(player, "<red>Command on cooldown for " + cooldowns[player.uniqueId]?.cooldown.toString() + " more seconds!</red>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BASS)
            return
        }

        if (pvpDisabled(player)) {
            messagePlayer(player, "PvP <green>Enabled!</green>")
            if (toggleCooldown > 0) { // Cooldown on disabling pvp after enabling it
                cooldowns[player.uniqueId] = ToggleCooldown(toggleCooldown, player, this)
                cooldowns[player.uniqueId]?.runTaskTimer(instance, 0, 20)
            }
        } else {
            messagePlayer(player, "PvP <red>Disabled!</red>")
        }

        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
        togglePvp(player)
    }

    fun removeCooldown(uuid: UUID) {
        cooldowns.remove(uuid)
    }
}