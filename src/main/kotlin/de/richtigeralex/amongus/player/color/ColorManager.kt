package de.richtigeralex.amongus.player.color

import org.bukkit.Color

class ColorManager {

    val availableColors: MutableList<Color>
    val usedColors: MutableList<Color>

    init {
        availableColors = arrayListOf()
        usedColors = availableColors
    }

    fun selectColor(newColor: Color, oldColor: Color) {
        availableColors.remove(newColor)
        availableColors.add(oldColor)

        usedColors.remove(oldColor)
        usedColors.add(newColor)
    }

    fun selectRandomAvailableColor(): Color {
        return availableColors.random()
    }

}