package io.github.lamelemon.yappp

import io.github.lamelemon.yappp.commands.PvpToggleCommand
import io.github.lamelemon.yappp.events.DamageEvent
import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.combatManager
import io.github.lamelemon.yappp.utils.Utils.instance
import io.github.lamelemon.yappp.utils.Utils.pvpStateKey
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin

import java.io.File

class Yappp : JavaPlugin() {

    lateinit var config: YamlConfiguration

    override fun onEnable() {

        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            saveResource("config.yml", false)
        }

        config = YamlConfiguration.loadConfiguration(configFile)
        val pluginManager = server.pluginManager

        if (!config.getBoolean("enabled", true)) {
            pluginManager.disablePlugin(this)
        }

        pvpStateKey = NamespacedKey(this, "pvpDisabled")
        instance = this
        combatManager = CombatManager(config.getLong("combat-duration", 0))

        registerCommand("pvp", PvpToggleCommand(config.getLong("toggle-cooldown", 0), config.getLong("toggle-timer", 0)))
        pluginManager.registerEvents(DamageEvent(config.getBoolean("self-pvp")), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
