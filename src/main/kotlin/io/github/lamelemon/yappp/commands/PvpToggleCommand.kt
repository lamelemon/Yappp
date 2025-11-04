package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.Yappp
import io.github.lamelemon.yappp.Yappp.Companion.togglePvp
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.entity.Player

class PvpToggleCommand(var timer: Int, var cooldown: Int) : BasicCommand {

    override fun execute(
        commandSourceStack: CommandSourceStack,
        args: Array<out String>
    ) {
        val sender = commandSourceStack.sender

        if (sender is Player) {
            sender.sendRichMessage("<lime>pvp toggled")
            togglePvp(sender)
        }
    }
}