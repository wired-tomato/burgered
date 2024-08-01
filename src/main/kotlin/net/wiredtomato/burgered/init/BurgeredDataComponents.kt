package net.wiredtomato.burgered.init

import net.minecraft.component.DataComponentType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.item.components.BurgerComponent
import net.wiredtomato.burgered.item.components.DirtyComponent
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent

object BurgeredDataComponents {
    val BURGER = register<BurgerComponent>("burger") {
        this.codec(BurgerComponent.CODEC).build()
    }

    val VANILLA_BURGER_INGREDIENT = register<VanillaBurgerIngredientComponent>("vanilla_burger_ingredient") {
        this.codec(VanillaBurgerIngredientComponent.CODEC).build()
    }

    val DIRTY = register<DirtyComponent>("dirty") {
        this.codec(DirtyComponent.CODEC).build()
    }

    fun <T> register(name: String, create: DataComponentType.Builder<T>.() -> DataComponentType<T>): DataComponentType<T> {
        return Registry.register(Registries.DATA_COMPONENT_TYPE, Burgered.id(name), DataComponentType.builder<T>().create())
    }
}