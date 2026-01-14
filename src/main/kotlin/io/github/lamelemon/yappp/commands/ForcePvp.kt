package io.github.lamelemon.yappp.commands

import io.github.lamelemon.yappp.Yappp.Companion.instance
import io.github.lamelemon.yappp.utils.Utils.enablePvp
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import io.papermc.paper.command.brigadier.BasicCommand
import io.papermc.paper.command.brigadier.CommandSourceStack
import org.bukkit.Sound
import org.bukkit.entity.Player

class ForcePvp : BasicCommand {

    override fun execute(
        commandSourceStack: CommandSourceStack,
        args: Array<out String>
    ) {
        val player = commandSourceStack.sender
        if (player !is Player) return

        // Get targeted players by their name
        for (name: String in args) {
            val target = instance.server.getPlayer(name)
            if (target is Player) { // Enable pvp for targets
                enablePvp(target, false)
            }
        }
        messagePlayer(player, "<green>Enabled Pvp for targets!</green>")
        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
    }

    override fun suggest(commandSourceStack: CommandSourceStack, args: Array<out String>): Collection<String> {
        val input = args.lastOrNull()?.lowercase() ?: ""
        return instance.server.onlinePlayers
            .map { it.name }
            .filter { it.lowercase().startsWith(input) }
    }

    override fun permission(): String {
        return "yappp.permission.forcepvp"
    }
}