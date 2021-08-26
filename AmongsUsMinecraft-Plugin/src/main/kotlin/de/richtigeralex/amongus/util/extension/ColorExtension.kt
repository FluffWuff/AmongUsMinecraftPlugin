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
package de.richtigeralex.amongus.util.extension

import org.bukkit.Color
import org.bukkit.Material

fun Color.toColorPrefix(): String {
    when(this) {
        Color.RED -> return "§cRot"
        Color.BLUE -> return "§9Blau"
        Color.GREEN -> return "§2Grün"
        Color.FUCHSIA -> return "§dPink"
        Color.ORANGE -> return "§6Orange"
        Color.YELLOW -> return "§eGelb"
        Color.BLACK -> return "§0Schwarz"
        Color.WHITE -> return "§fWeiß"
        Color.PURPLE -> return "§5Lila"
        Color.GRAY -> return "§7Grau"
        Color.AQUA -> return "§bCyan"
        Color.LIME -> return "§aLime"
    }
    return "§fTest"
}

fun Color.toConcreteMaterial(): Material {
    when (this) {
        Color.RED -> return Material.RED_CONCRETE
        Color.BLUE -> return Material.BLUE_CONCRETE
        Color.GREEN -> return Material.GREEN_CONCRETE
        Color.FUCHSIA -> return Material.PINK_CONCRETE
        Color.ORANGE -> return Material.ORANGE_CONCRETE
        Color.YELLOW -> return Material.YELLOW_CONCRETE
        Color.BLACK -> return Material.BLACK_CONCRETE
        Color.WHITE -> return Material.WHITE_CONCRETE
        Color.PURPLE -> return Material.PURPLE_CONCRETE
        Color.GRAY -> return Material.GRAY_CONCRETE
        Color.AQUA -> return Material.LIGHT_BLUE_CONCRETE
        Color.LIME -> return Material.LIME_CONCRETE
    }
    return Material.NETHER_STAR
}

fun Color.toColorSlotInventory(): Int {
    when (this) {
        Color.RED -> return 10
        Color.BLUE -> return 11
        Color.GREEN -> return 12
        Color.FUCHSIA -> return 14
        Color.ORANGE -> return 15
        Color.YELLOW -> return 16
        Color.BLACK -> return 19
        Color.WHITE -> return 20
        Color.PURPLE -> return 21
        Color.GRAY -> return 23
        Color.AQUA -> return 24
        Color.LIME -> return 25
    }
    return 0
}