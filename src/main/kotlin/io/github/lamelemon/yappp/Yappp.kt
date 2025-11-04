package io.github.lamelemon.yappp

import io.github.lamelemon.yappp.commands.PvpToggleCommand
import io.github.lamelemon.yappp.events.DamageEvent
import org.bukkit.Bukkit
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitScheduler
import java.io.File
import java.util.Objects

class Yappp : JavaPlugin() {

    lateinit var config: YamlConfiguration

    companion object {
        lateinit var instance: Yappp
        lateinit var pvpStateKey: NamespacedKey

        fun pvpDisabled(player: Player): Boolean {
            return Objects.requireNonNullElse(player.persistentDataContainer.get(pvpStateKey, PersistentDataType.BOOLEAN), false)
        }

        fun togglePvp(player: Player) {
            Bukkit.getScheduler().runTaskLater(instance, Runnable {
                player.persistentDataContainer.set(pvpStateKey, PersistentDataType.BOOLEAN, !pvpDisabled(player))
                return@Runnable
            }, 100)
        }
    }

    override fun onEnable() {
        instance = this
        pvpStateKey = NamespacedKey(this, "pvpDisabled")
        config = YamlConfiguration.loadConfiguration(File(dataFolder, "config.yml"))

        registerCommand("pvp", PvpToggleCommand(config.getInt("toggle-timer", 0), config.getInt("toggle-cooldown", 0)))

        server.pluginManager.registerEvents(DamageEvent(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }


}
