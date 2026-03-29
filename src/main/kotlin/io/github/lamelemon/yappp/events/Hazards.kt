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

class Hazards(val immunityTimeout: Long, hazardStrings: List<String>): Listener {

    companion object {
        val placedBlock: HashSet<Block> = HashSet()

        fun isHazardous(block: Block): Boolean {
            return placedBlock.contains(block)
        }
    }

    private var hazards: HashSet<Material> = hazardStrings.mapNotNull { Material.getMaterial(it) }.toHashSet()

    @EventHandler(priority = EventPriority.MONITOR)
    fun playerPlaceLava(event: BlockPlaceEvent) {
        if (event.isCancelled) return

        val block = event.blockPlaced
        if (!hazards.contains(block.type)) return

        addHazard(block)
    }

    @EventHandler(priority = EventPriority.MONITOR)
    fun lavaFlowEvent(event: BlockFromToEvent) {
        if (event.isCancelled) return

        val from = event.block
        val to = event.toBlock
        if (hazards.contains(from.type) && hazards.contains(to.type) && placedBlock.contains(from)) addHazard(to)
    }

    private fun addHazard(block: Block) {
        placedBlock.add(block)
        Bukkit.getScheduler().runTaskLater(Yappp.instance, Runnable{
            placedBlock.remove(block)
        }, immunityTimeout)
    }
}