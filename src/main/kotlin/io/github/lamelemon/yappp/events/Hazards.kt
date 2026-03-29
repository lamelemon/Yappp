package io.github.lamelemon.yappp.events

import io.github.lamelemon.yappp.Yappp
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockFromToEvent
import org.bukkit.event.block.BlockPlaceEvent
import kotlin.collections.get

class Hazards(val immunityTimeout: Long): Listener {

    private val placedBlock: HashSet<Block> = HashSet()

    @EventHandler(priority = EventPriority.MONITOR)
    fun playerPlaceLava(event: BlockPlaceEvent) {
        if (event.isCancelled) return

        val block = event.blockPlaced
        if (block.type != Material.LAVA) return

        val player = event.player
        if (!placedLava.contains(player)) placedLava[player] = hashSetOf(block)
        else placedLava[player]?.add(block)

        Bukkit.getScheduler().runTaskLater(Yappp.Companion.instance, Runnable{
            placedLava[player]?.remove(block)
        }, immunityTimeout)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun lavaFlowEvent(event: BlockFromToEvent) {
        if (event.isCancelled) return

        val from = event.block
        val to = event.block
        if (!placedLava.contains(from)) return
    }
}