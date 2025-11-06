package io.github.lamelemon.yappp.utils.timers

import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.sendActionBar
import io.github.lamelemon.yappp.utils.Utils.simplePlaySound
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class CombatTimer(var combatDuration: Long, val player: Player) : BukkitRunnable() {

    override fun run() {
        combatDuration--
        if (combatDuration <= 0) { // End timer
            messagePlayer(player, "<green>Exited combat!</green>")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
            combatManager.removeTimer(player.uniqueId)
            this.cancel()
        }
        sendActionBar(player, "<red>In combat for " + combatDuration + "s!</red>")
    }
}