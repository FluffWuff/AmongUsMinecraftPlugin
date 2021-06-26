package de.richtigeralex.amongus.listener

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.entity.FoodLevelChangeEvent
import org.bukkit.event.weather.ThunderChangeEvent
import org.bukkit.event.weather.WeatherChangeEvent

class CancelUselessListener : Listener {

    @EventHandler
    fun handlePlayerFoodLevel(event: FoodLevelChangeEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun handleWeatherChange(event: WeatherChangeEvent) {
        event.world.clearWeatherDuration
        event.isCancelled = true
    }

    @EventHandler
    fun handleThunderstormChange(event: ThunderChangeEvent) {
        event.world.clearWeatherDuration
        event.isCancelled = true
    }

    @EventHandler
    fun handleBlockBreak(event: BlockBreakEvent) {
        event.isCancelled = true
    }

    @EventHandler
    fun handleBlockPlace(event: BlockPlaceEvent) {
        event.isCancelled = true
    }
}