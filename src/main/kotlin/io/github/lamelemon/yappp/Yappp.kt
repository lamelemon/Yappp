package io.github.lamelemon.yappp

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class Yappp : JavaPlugin(), Listener {

    override fun onEnable() {
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerTakeDamage(event: EntityDamageEvent) {
        val causingEntity = event.damageSource.causingEntity
        val victim = event.entity

        if (victim is Player && causingEntity is Player) {
            event.isCancelled = true
        }

    }
}
