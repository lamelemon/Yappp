package io.github.lamelemon.yappp.utils

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import java.util.*

object Utils {
    lateinit var pvpStateKey: NamespacedKey

    fun pvpDisabled(player: Player): Boolean {
        return Objects.requireNonNullElse(player.persistentDataContainer.get(pvpStateKey, PersistentDataType.BOOLEAN), false)
    }

    fun enablePvp(player: Player) {
        messagePlayer(player, "PvP <green>Enabled</green>!")
        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
        player.persistentDataContainer.set(pvpStateKey, PersistentDataType.BOOLEAN, false)
    }

    fun disablePvp(player: Player) {
        messagePlayer(player, "PvP <red>Disabled</red>!")
        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
        player.persistentDataContainer.set(pvpStateKey, PersistentDataType.BOOLEAN, true)
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