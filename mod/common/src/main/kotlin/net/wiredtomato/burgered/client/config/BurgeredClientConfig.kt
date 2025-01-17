package net.wiredtomato.burgered.client.config

import kotlinx.serialization.Serializable
import net.minecraft.client.gui.screens.Screen
import net.wiredtomato.burgered.api.config.SimpleJsonConfig
import net.wiredtomato.burgered.service.PlatformServiceImpl
import net.wiredtomato.burgered.service.client.ConfigServiceImpl

@Serializable
data class BurgeredClientConfig(
    val rendering: Rendering = Rendering()
) {
    @Serializable
    data class Rendering(
        var maxRenderedBurgerIngredients: Int = 256,
        var maxSloppinessRotationX: Float = 10f,
        var maxSloppinessRotationY: Float = 90f,
        var maxSloppinessRotationZ: Float = 10f,
        var renderNoTransform: Boolean = true
    )

    companion object {
        val CONFIG = SimpleJsonConfig(::BurgeredClientConfig, serializer()) {
            PlatformServiceImpl.getConfigDir().resolve("burgered/burgered-client.json")
        }

        var maxRenderedBurgerIngredients
            get() = CONFIG.instance().rendering.maxRenderedBurgerIngredients
            set(value) {
                CONFIG.instance().rendering.maxRenderedBurgerIngredients = value
            }

        var maxSloppinessRotationX
            get() = CONFIG.instance().rendering.maxSloppinessRotationX
            set(value) {
                CONFIG.instance().rendering.maxSloppinessRotationX = value
            }

        var maxSloppinessRotationY
            get() = CONFIG.instance().rendering.maxSloppinessRotationY
            set(value) {
                CONFIG.instance().rendering.maxSloppinessRotationY = value
            }

        var maxSloppinessRotationZ
            get() = CONFIG.instance().rendering.maxSloppinessRotationZ
            set(value) {
                CONFIG.instance().rendering.maxSloppinessRotationZ = value
            }

        fun createScreen(parent: Screen?): Screen {
            return ConfigServiceImpl.createConfigScreen(parent)
        }
    }
}
