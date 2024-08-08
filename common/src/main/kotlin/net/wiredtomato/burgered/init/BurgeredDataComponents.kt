package net.wiredtomato.burgered.init

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.registries.Registries
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.registry.RegistryDelegate
import net.wiredtomato.burgered.api.registry.registered
import net.wiredtomato.burgered.item.components.BurgerComponent
import net.wiredtomato.burgered.item.components.DirtyComponent
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent

object BurgeredDataComponents {
    val DATA_COMPONENTS = DeferredRegister.create(Burgered.MOD_ID, Registries.DATA_COMPONENT_TYPE)

    val BURGER by registering<BurgerComponent>("burger") {
        this.persistent(BurgerComponent.CODEC).build()
    }

    val VANILLA_BURGER_INGREDIENT by registering<VanillaBurgerIngredientComponent>("vanilla_burger_ingredient") {
        this.persistent(VanillaBurgerIngredientComponent.CODEC).build()
    }

    val DIRTY by registering<DirtyComponent>("dirty") {
        this.persistent(DirtyComponent.CODEC).build()
    }

    fun <T> registering(name: String, create: DataComponentType.Builder<T>.() -> DataComponentType<T>): RegistryDelegate<BurgeredDataComponents, DataComponentType<T>> {
        return registered(DATA_COMPONENTS.register(Burgered.modLoc(name)) { DataComponentType.builder<T>().create() })
    }
}
