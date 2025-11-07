package io.github.lamelemon.yappp.utils

import io.github.lamelemon.yappp.Yappp
import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.Objects

object Utils {
    lateinit var pvpStateKey: NamespacedKey
    lateinit var instance: Yappp
    lateinit var combatManager: CombatManager

    fun pvpDisabled(player: Player): Boolean {
        return Objects.requireNonNullElse(player.persistentDataContainer.get(pvpStateKey, PersistentDataType.BOOLEAN), false)
    }

    fun togglePvp(player: Player) {
        player.persistentDataContainer.set(pvpStateKey, PersistentDataType.BOOLEAN, !pvpDisabled(player))
    }

    fun enablePvp(player: Player) {
        player.persistentDataContainer.set(pvpStateKey, PersistentDataType.BOOLEAN, false)
    }

    fun messagePlayer(player: Player, message: String) {
        player.sendRichMessage("<gold>[</gold><color:#fafad2>Yappp</color><gold>]</gold> $message")
    }

    fun sendActionBar(player: Player, message: String) {
        player.sendActionBar(MiniMessage.miniMessage().deserialize(message))
    }

    fun simplePlaySound(player: Player, sound: Sound) {
        player.playSound(player.location,
            sound,
            1.0f,
            1.0f
        )
    }
}