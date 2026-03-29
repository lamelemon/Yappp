package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.timers.LavaImmunityTimer
import org.bukkit.Material
import org.bukkit.block.BlockType
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockPlaceEvent

class PlayerPlaceLava(val immunityTimeout: Long): Listener {

    companion object {
        val placedLava: HashSet<LavaImmunityTimer> = HashSet()
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun playerPlaceLava(event: BlockPlaceEvent) {
        if (event.isCancelled) return

        val block = event.blockPlaced
        if (block.type != Material.LAVA) return

        placedLava.add(LavaImmunityTimer())
    }
}