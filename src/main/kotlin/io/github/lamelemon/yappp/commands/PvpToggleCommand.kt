package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.utils.PvpUtils.combatManager
import io.github.lamelemon.yappp.utils.PvpUtils.messagePlayer
import io.github.lamelemon.yappp.utils.PvpUtils.pvpDisabled
import io.github.lamelemon.yappp.utils.PvpUtils.togglePvp
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player

class PvpToggleCommand(val toggleCooldown: Double) : BasicCommand {

    override fun execute(
        commandSourceStack: CommandSourceStack,
        args: Array<out String>
    ) {
        val player = commandSourceStack.sender
        if (player !is Player) return

        if (combatManager.isInCombat(player)){
            messagePlayer(player, "<red>In Combat!</red>")
            return
        }

        if (pvpDisabled(player)) {
            messagePlayer(player, "PvP <green>Enabled!</green>")
        } else {
            messagePlayer(player, "PvP <red>Disabled!</red>")
        }
        togglePvp(player)
    }
}