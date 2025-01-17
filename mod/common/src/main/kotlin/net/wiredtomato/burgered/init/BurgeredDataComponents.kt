package net.wiredtomato.burgered.init

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.BuiltInRegistries
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.data.component.BurgerComponent
import net.wiredtomato.burgered.api.data.component.IngredientQualityComponent
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.service.RegistryServiceImpl

object BurgeredDataComponents {
    val BURGER: DataComponentType<BurgerComponent> by registering("burger") {
        persistent(BurgerComponent.CODEC).build()
    }

    val QUALITY: DataComponentType<IngredientQualityComponent> by registering("quality") {
        persistent(IngredientQualityComponent.CODEC).build()
    }

    fun <T> registering(name: String, create: DataComponentType.Builder<T>.() -> DataComponentType<T>): KSupplier<DataComponentType<T>> {
        return (RegistryServiceImpl.register(BuiltInRegistries.DATA_COMPONENT_TYPE, Burgered.modLoc(name)) { DataComponentType.builder<T>().create() })
    }
}