package io.github.lamelemon.yappp

import io.github.lamelemon.yappp.commands.PvpToggleCommand
import io.github.lamelemon.yappp.events.DamageEvent
import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.PvpUtils.combatManager
import io.github.lamelemon.yappp.utils.PvpUtils.instance
import io.github.lamelemon.yappp.utils.PvpUtils.pvpStateKey
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
        combatManager = CombatManager(config.getLong("combat-duration", 0) * 20)

        registerCommand("pvp", PvpToggleCommand(config.getDouble("toggle-cooldown", 0.0)))
        pluginManager.registerEvents(DamageEvent(), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
