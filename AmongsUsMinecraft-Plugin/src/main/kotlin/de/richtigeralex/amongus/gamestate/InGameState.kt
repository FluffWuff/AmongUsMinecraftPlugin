package de.richtigeralex.amongus.gamestate

import de.richtigeralex.amongus.AmongUs
import de.richtigeralex.amongus.map.IAmongUsMapManager
import de.richtigeralex.amongus.player.AmongUsPlayer
import de.richtigeralex.amongus.player.AmongUsPlayerManager
import de.richtigeralex.amongus.player.ImposterPlayer
import de.richtigeralex.amongus.util.extension.toColorPrefix
import de.richtigeralex.amongus.util.itembuilder.ItemBuilder
import kotlinx.coroutines.*
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.entity.ArmorStand
import kotlin.math.roundToInt

class InGameState(val amongUsPlayerManager: AmongUsPlayerManager, val amongUsMapManager: IAmongUsMapManager) : GameState() {

    private val startEndSequenceArmorStands: MutableList<ArmorStand> = mutableListOf()
    var sequenceType: AmongUsSequenceType? = null

    override fun start() {
        sequenceType = AmongUsSequenceType.START_ROUND

        amongUsPlayerManager.inGamePlayers.forEach { p1 ->
            amongUsPlayerManager.inGamePlayers.forEach { p2 ->
                if (p1 != p2) p1.player.hidePlayer(AmongUs.instance, p2.player)
            }
        }

        showSequence()

        Thread.sleep(4_000L)

        amongUsPlayerManager.inGamePlayers.forEach { it.player.inventory.heldItemSlot = 4 }
        amongUsPlayerManager.inGamePlayers.filterIsInstance(ImposterPlayer::class.java).forEach {
            it.player.inventory.setItem(1, ItemBuilder(material = Material.IRON_SWORD, displayName = "§cKill").build())
        }

        amongUsPlayerManager.inGamePlayers.forEach { p1 ->
            amongUsPlayerManager.inGamePlayers.forEach { p2 ->
                if (p1 != p2) p1.player.showPlayer(AmongUs.instance, p2.player)
            }
        }
    }

    override fun stop() {

    }

    private fun showSequence() {
        val selectedMap = amongUsMapManager.selectedMap!!
        val spawnLocations: MutableList<Location> = mutableListOf()
        when (sequenceType) {
            null -> return
            AmongUsSequenceType.START_ROUND -> {
                val midPosition = selectedMap.startSequenceLocation.blockX
                val n = amongUsPlayerManager.inGamePlayers.size
                var startPosition =
                    when {
                        n == 1 -> 0
                        n % 2 == 0 -> ((midPosition + n) / 2).toDouble().roundToInt()
                        else -> (n - 1) / 2
                    }
                do {
                    spawnLocations.add(
                        Location(
                            selectedMap.startSequenceLocation.world,
                            startPosition.toDouble() + 0.5,
                            selectedMap.startSequenceLocation.blockY.toDouble(),
                            selectedMap.startSequenceLocation.blockZ.toDouble() + 5.5, // TODO ADD CORRECT NUMBER TO Z-COORDINATE
                            getArmorStandYaw(selectedMap.startSequenceLocation),
                            0.0F
                        )
                    )
                    startPosition -= 1
                } while (n > spawnLocations.size)

                spawnLocations.forEach {
                    startEndSequenceArmorStands.add(it.world!!.spawn(it, ArmorStand::class.java))
                }

                for (i in startEndSequenceArmorStands.indices) {
                    val armoursStand = startEndSequenceArmorStands[i]

                    armoursStand.setArms(true)
                    val color = amongUsPlayerManager.inGamePlayers[i].color
                    armoursStand.equipment!!.helmet = ItemBuilder(material = Material.LEATHER_HELMET, color = color).build()
                    armoursStand.equipment!!.chestplate = ItemBuilder(material = Material.LEATHER_CHESTPLATE, color = color).build()
                    armoursStand.equipment!!.leggings = ItemBuilder(material = Material.LEATHER_LEGGINGS, color = color).build()
                    armoursStand.equipment!!.boots = ItemBuilder(material = Material.LEATHER_BOOTS, color = color).build()
                    armoursStand.customName = color.toColorPrefix() + " " + amongUsPlayerManager.inGamePlayers[i].player.name
                    armoursStand.isCustomNameVisible = true
                }
                amongUsPlayerManager.inGamePlayers.forEach { it.player.teleport(amongUsMapManager.selectedMap!!.startSequenceLocation) }
            }

            AmongUsSequenceType.IMPOSTER_WIN -> {

            }

            AmongUsSequenceType.CREWMATE_WIN -> {

            }
        }
    }

    private fun getArmorStandYaw(location: Location): Float {
        return when(location.yaw) {
            0.0F -> -180.0F
            -180.0F -> 0.0F
            90.0F -> -90.0F
            -90.0F -> 90.0F
            else -> 0.0F
        }
    }


    enum class AmongUsSequenceType {
        START_ROUND,
        IMPOSTER_WIN,
        CREWMATE_WIN
    }

}
