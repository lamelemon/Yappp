package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.utils.PvpUtils.combatManager
import io.github.lamelemon.yappp.utils.PvpUtils.messagePlayer
import io.github.lamelemon.yappp.utils.PvpUtils.togglePvp
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player

class PvpToggleCommand(val toggleCooldown: Double) : BasicCommand {

    override fun execute(
        commandSourceStack: CommandSourceStack,
        args: Array<out String>
    ) {
        val sender = commandSourceStack.sender
        if (sender !is Player) return

        if (combatManager.isInCombat(sender)){
            messagePlayer(sender, "<red>In combat!</red>")
            return
        }

        messagePlayer(sender, "<green>Pvp toggled!</green>")
        togglePvp(sender)
    }
}