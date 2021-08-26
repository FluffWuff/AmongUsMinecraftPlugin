/*
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <https://www.gnu.org/licenses/>.
 */
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