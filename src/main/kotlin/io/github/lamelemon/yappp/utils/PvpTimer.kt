package io.github.lamelemon.yappp.utils

import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.instance
import io.github.lamelemon.yappp.utils.Utils.messagePlayer
import io.github.lamelemon.yappp.utils.Utils.sendActionBar
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitRunnable

class PvpTimer(combatDuration: Long, val player: Player) : BukkitRunnable() {
    var duration = combatDuration

    override fun run() {
        duration--
        if (duration > 0) {
            instance.logger.info("sending action bar to " + player.name)
        } else {
            messagePlayer(player, "<green>Exited combat!</green>")
            instance.logger.info("ending timer for " + player.name)
            combatManager.removeTimer(player.uniqueId)
            this.cancel()
        }
        sendActionBar(player, "<red>In combat for " + duration + "s!</red>")
    }
}