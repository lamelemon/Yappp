package io.github.lamelemon.yappp.utils

import net.kyori.adventure.text.minimessage.MiniMessage
import org.bukkit.NamespacedKey
import org.bukkit.Sound
import org.bukkit.entity.Player
import java.util.*

object Utils {
    lateinit var pvpStateKey: NamespacedKey
    private val pvpStates = mutableMapOf<UUID, Boolean>()

    fun pvpEnabled(player: Player): Boolean {
        return Objects.requireNonNullElse(pvpStates[player.uniqueId], false)
    }

    fun setPvp(player: Player, notify: Boolean, newState: Boolean) {
        if (notify) {
            if (newState) messagePlayer(player, "PvP <green>Enabled</green>!")
            else messagePlayer(player, "PvP <red>Disabled</red>!")
        }

        pvpStates[player.uniqueId] = newState
        simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
    }

    fun enablePvp(player: Player, notify: Boolean) {
        if (notify) {
            messagePlayer(player, "PvP <green>Enabled</green>!")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
        }

        pvpStates[player.uniqueId] = true
    }

    fun disablePvp(player: Player, notify: Boolean) {
        if (notify) {
            messagePlayer(player, "PvP <red>Disabled</red>!")
            simplePlaySound(player, Sound.BLOCK_NOTE_BLOCK_PLING)
        }

        pvpStates[player.uniqueId] = false
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