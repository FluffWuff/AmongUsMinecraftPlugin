package de.richtigeralex.amongus.player.color

import org.bukkit.Color
import org.bukkit.Material

object ColorManager {

    val availableColors: MutableList<Color> = mutableListOf(Color.RED, Color.BLUE, Color.GREEN, Color.FUCHSIA, Color.ORANGE, Color.YELLOW, Color.BLACK, Color.WHITE, Color.PURPLE, Color.GRAY, Color.AQUA, Color.LIME)
    val usedColors: MutableList<Color> = mutableListOf()

    fun selectColor(newColor: Color, oldColor: Color) {
        availableColors.remove(newColor)
        availableColors.add(oldColor)

        usedColors.remove(oldColor)
        usedColors.add(newColor)
    }

    /**
     * ONLY USE THIS IF A PLAYER LEAVES
     */
    fun unselectColor(color: Color) {
        usedColors.remove(color)
        availableColors.add(color)
    }

    fun selectRandomAvailableColor(): Color {
        val color: Color = availableColors.random()
        availableColors.remove(color)
        usedColors.add(color)
        return color
    }

}