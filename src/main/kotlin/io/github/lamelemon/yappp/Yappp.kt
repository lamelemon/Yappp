package io.github.lamelemon.yappp

import io.github.lamelemon.yappp.api.YapppApi
import io.github.lamelemon.yappp.commands.ForcePvp
import io.github.lamelemon.yappp.commands.PvpToggle
import io.github.lamelemon.yappp.events.Hazards
import io.github.lamelemon.yappp.events.PlayerDeath
import io.github.lamelemon.yappp.events.PlayerTakeDamage
import io.github.lamelemon.yappp.internal.APImplementation
import io.github.lamelemon.yappp.utils.CombatManager
import io.github.lamelemon.yappp.utils.Utils.pvpStateKey
import org.bukkit.NamespacedKey
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

// TODO /duel <player> to challenge people to fights (disable pvp between fighters and outsiders, tag fighters)
// TODO punish players for combat logging

class Yappp : JavaPlugin() {

    companion object {
        lateinit var instance: Yappp
    }

    lateinit var api: YapppApi
        private set

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

        server.servicesManager.register(
            YapppApi::class.java,
            APImplementation(),
            this,
            ServicePriority.Normal
        )

        pvpStateKey = NamespacedKey(this, "pvpDisabled")
        instance = this
        CombatManager.setTagDuration(config.getLong("combat-duration", 0))

        registerCommand(
            "pvp",
            config.getStringList("toggle.aliases"),
            PvpToggle(
                config.getLong("toggle.cooldown", 0),
                config.getLong("toggle.timer", 0)
            )
        )
        registerCommand("forcePvp", config.getStringList("force-pvp.aliases"), ForcePvp())

        pluginManager.registerEvents(
            PlayerTakeDamage(
                config.getBoolean("combat.self-damage", true)
            ),
            this)
        pluginManager.registerEvents(
            PlayerDeath(
                config.getBoolean("combat.keep-inventory", false),
                config.getBoolean("combat.disable-pvp-on-death", true),
                config.getBoolean("environment.fire-death-counts", true)
            ),
            this
        )
        if (config.getBoolean("lava.enabled", true)) {
            pluginManager.registerEvents(
                Hazards(
                    config.getLong("lava-timeout", 5) * 20,
                    config.getStringList("environment.hazard.hazards")
                ),
                this
            )
        }
    }

    override fun onDisable() {
        server.servicesManager.unregisterAll(this)
    }
}
