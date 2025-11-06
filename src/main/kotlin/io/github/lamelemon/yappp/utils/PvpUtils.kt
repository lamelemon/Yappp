package io.github.lamelemon.yappp.utils

import io.github.lamelemon.yappp.Yappp
import org.bukkit.NamespacedKey
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.Objects

object PvpUtils {
    lateinit var pvpStateKey: NamespacedKey
    lateinit var instance: Yappp
    lateinit var combatManager: CombatManager

    fun pvpDisabled(player: Player): Boolean {
        return Objects.requireNonNullElse(player.persistentDataContainer.get(pvpStateKey, PersistentDataType.BOOLEAN), false)
    }

    fun togglePvp(player: Player) {
        player.persistentDataContainer.set(pvpStateKey, PersistentDataType.BOOLEAN, !pvpDisabled(player))
    }

    fun messagePlayer(player: Player, message: String) {
        player.sendRichMessage("<gold>[</gold><color:#fafad2>Yappp</color><gold>]</gold> $message")
    }
}