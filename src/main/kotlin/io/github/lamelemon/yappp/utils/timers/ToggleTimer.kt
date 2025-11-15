package io.github.lamelemon.yappp.utils.timers

import io.github.lamelemon.yappp.commands.PvpToggle
import io.github.lamelemon.yappp.utils.Utils.disablePvp
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class ToggleTimer(var duration : Long, val player: Player, val pvpToggle: PvpToggle) : BukkitRunnable() {

    init {
        messagePlayer(player, "<red>Disabling</red> PvP in...")
    }

    override fun run() {
        if (duration <= 0) {
            disablePvp(player)
            pvpToggle.toggleTimers.remove(player.uniqueId)
            this.cancel()
            return
        }
        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_BIT)
        messagePlayer(player, "$duration")
        duration--
    }
}