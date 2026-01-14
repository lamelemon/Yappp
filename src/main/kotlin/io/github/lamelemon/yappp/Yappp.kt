package io.github.lamelemon.yappp

import io.github.lamelemon.yappp.commands.ForcePvp
import io.github.lamelemon.yappp.commands.PvpToggle
import io.github.lamelemon.yappp.events.PlayerDeath
import io.github.lamelemon.yappp.events.PlayerTakeDamage
import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.pvpStateKey
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

// TODO /duel <player> to challenge people to fights (disable pvp between fighters and outsiders, tag fighters)
// TODO punish players for combat logging

class Yappp : JavaPlugin() {

    companion object {
        lateinit var instance: Yappp
    }

    override fun onEnable() {
        val configFile = File(dataFolder, "config.yml")
        if (!configFile.exists()) {
            saveResource("config.yml", false)
        }

        val config = YamlConfiguration.loadConfiguration(configFile)
        val pluginManager = server.pluginManager

        if (!config.getBoolean("enabled", true)) {
            pluginManager.disablePlugin(this)
        }

        pvpStateKey = NamespacedKey(this, "pvpDisabled")
        instance = this
        CombatManager.setTagDuration(config.getLong("combat-duration", 0))

        registerCommand("pvp", config.getStringList("toggle-command-aliases"),PvpToggle(config.getLong("toggle-cooldown", 0), config.getLong("toggle-timer", 0)))
        registerCommand("forcePvp", ForcePvp())

        pluginManager.registerEvents(PlayerTakeDamage(config.getBoolean("self-damage")), this)
        pluginManager.registerEvents(PlayerDeath(config.getBoolean("keep-inventory", false), config.getBoolean("disable-pvp-on-death", true)), this)
    }
}
