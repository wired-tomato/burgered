package net.wiredtomato.burgered.neoforge.service.client

import deplatformed.ServiceImpl
import me.shedaniel.clothconfig2.api.ConfigBuilder
import net.minecraft.client.gui.screens.Screen
import net.minecraft.network.chat.Component
import net.wiredtomato.burgered.client.config.BurgeredClientConfig.Companion.CONFIG
import net.wiredtomato.burgered.client.config.BurgeredClientConfig.Companion.maxRenderedBurgerIngredients
import net.wiredtomato.burgered.client.config.BurgeredClientConfig.Companion.maxSloppinessRotationX
import net.wiredtomato.burgered.client.config.BurgeredClientConfig.Companion.maxSloppinessRotationY
import net.wiredtomato.burgered.client.config.BurgeredClientConfig.Companion.maxSloppinessRotationZ
import net.wiredtomato.burgered.service.client.ConfigService

@ServiceImpl(ConfigService::class)
class NeoForgeConfigService : ConfigService {
    override fun createConfigScreen(parentScreen: Screen?): Screen {
        val builder = ConfigBuilder.create()
            .setParentScreen(parentScreen)
            .setTitle(Component.translatable("config.burgered.client.title"))

        val entries = builder.entryBuilder()

        val rendering =
            builder.getOrCreateCategory(Component.translatable("config.burgered.client.category.rendering"))
        rendering.setDescription(arrayOf(Component.translatable("config.burgered.client.category.rendering.description")))

        rendering.addEntry(entries.startIntField(
            Component.translatable("config.burgered.maxRenderedBurgerIngredients"),
            maxRenderedBurgerIngredients
        )
            .setMin(0)
            .setMax(2048)
            .setDefaultValue(256)
            .setTooltip(Component.translatable("config.burgered.maxRenderedBurgerIngredients.tooltip"))
            .setSaveConsumer { maxRenderedBurgerIngredients = it }
            .build())

        rendering.addEntry(entries.startFloatField(
            Component.translatable("config.burgered.maxSloppinessRotationX"),
            maxSloppinessRotationX
        )
            .setMin(0f)
            .setMax(360f)
            .setDefaultValue(10f)
            .setTooltip(Component.translatable("config.burgered.maxSloppinessRotationX.tooltip"))
            .setSaveConsumer { maxSloppinessRotationX = it }
            .build())

        rendering.addEntry(entries.startFloatField(
            Component.translatable("config.burgered.maxSloppinessRotationY"),
            maxSloppinessRotationY
        )
            .setMin(0f)
            .setMax(360f)
            .setDefaultValue(90f)
            .setTooltip(Component.translatable("config.burgered.maxSloppinessRotationY.tooltip"))
            .setSaveConsumer { maxSloppinessRotationY = it }
            .build())

        rendering.addEntry(entries.startFloatField(
            Component.translatable("config.burgered.maxSloppinessRotationZ"),
            maxSloppinessRotationZ
        )
            .setMin(0f)
            .setMax(360f)
            .setDefaultValue(10f)
            .setTooltip(Component.translatable("config.burgered.maxSloppinessRotationZ.tooltip"))
            .setSaveConsumer { maxSloppinessRotationZ = it }
            .build())

        builder.setSavingRunnable { CONFIG.save() }

        return builder.build()
    }
}