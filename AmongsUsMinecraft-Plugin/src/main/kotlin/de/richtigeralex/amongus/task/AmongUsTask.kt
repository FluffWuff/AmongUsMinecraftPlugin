package de.richtigeralex.amongus.task

import org.bukkit.Location

class AmongUsTaskManager {

}

interface AmongUsTask {

    val taskName: String
    val locations: MutableList<Location>

    fun solveTask()

}