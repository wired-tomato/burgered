package net.wiredtomato.burgered.client.config

import dev.architectury.platform.Platform
import kotlinx.serialization.Serializable
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.ChatFormatting
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.wiredtomato.burgered.api.config.SimpleJsonConfig

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
            Platform.getConfigFolder().resolve("burgered/burgered-client.json")
        }

        var maxRenderedBurgerIngredients by CONFIG.getSubProperty<Companion, Int>("rendering", "maxRenderedBurgerIngredients")
        var maxSloppinessRotationX by CONFIG.getSubProperty<Companion, Float>("rendering", "maxSloppinessRotationX")
        var maxSloppinessRotationY by CONFIG.getSubProperty<Companion, Float>("rendering", "maxSloppinessRotationY")
        var maxSloppinessRotationZ by CONFIG.getSubProperty<Companion, Float>("rendering", "maxSloppinessRotationZ")
        var renderNoTransform by CONFIG.getSubProperty<Companion, Boolean>("rendering", "renderNoTransform")

        fun createScreen(parent: Screen?): Screen {
            val builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("config.burgered.client.title"))

            val entries = builder.entryBuilder()

            val rendering = builder.getOrCreateCategory(Component.translatable("config.burgered.client.category.rendering"))
            rendering.setDescription(arrayOf(Component.translatable("config.burgered.client.category.rendering.description")))

            rendering.addEntry(entries.startIntField(Component.translatable("config.burgered.maxRenderedBurgerIngredients"), maxRenderedBurgerIngredients)
                .setMin(0)
                .setMax(2048)
                .setDefaultValue(256)
                .setTooltip(Component.translatable("config.burgered.maxRenderedBurgerIngredients.tooltip"))
                .setSaveConsumer { maxRenderedBurgerIngredients = it }
                .build())

            rendering.addEntry(entries.startFloatField(Component.translatable("config.burgered.maxSloppinessRotationX"), maxSloppinessRotationX)
                .setMin(0f)
                .setMax(360f)
                .setDefaultValue(10f)
                .setTooltip(Component.translatable("config.burgered.maxSloppinessRotationX.tooltip"))
                .setSaveConsumer { maxSloppinessRotationX = it }
                .build())

            rendering.addEntry(entries.startFloatField(Component.translatable("config.burgered.maxSloppinessRotationY"), maxSloppinessRotationY)
                .setMin(0f)
                .setMax(360f)
                .setDefaultValue(90f)
                .setTooltip(Component.translatable("config.burgered.maxSloppinessRotationY.tooltip"))
                .setSaveConsumer { maxSloppinessRotationY = it }
                .build())

            rendering.addEntry(entries.startFloatField(Component.translatable("config.burgered.maxSloppinessRotationZ"), maxSloppinessRotationZ)
                .setMin(0f)
                .setMax(360f)
                .setDefaultValue(10f)
                .setTooltip(Component.translatable("config.burgered.maxSloppinessRotationZ.tooltip"))
                .setSaveConsumer { maxSloppinessRotationZ = it }
                .build())

            rendering.addEntry(entries.startBooleanToggle(Component.translatable("config.burgered.renderNoTransform"), renderNoTransform)
                .setDefaultValue(true)
                .setTooltip(
                    Component.translatable("config.burgered.renderNoTransform.tooltip.line1"),
                    Component.translatable("config.burgered.renderNoTransform.tooltip.line2").withStyle(ChatFormatting.RED)
                )
                .setSaveConsumer { renderNoTransform = it }
                .build())

            builder.setSavingRunnable { CONFIG.save() }

            return builder.build()
        }
    }
}
