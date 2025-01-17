package net.wiredtomato.burgered.api.registry

import net.minecraft.resources.ResourceKey
import net.wiredtomato.burgered.Burgered.modLoc
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredient
import net.wiredtomato.burgered.service.RegistryServiceImpl

object BurgeredRegistries {
    init {
        RegistryServiceImpl.createDynamicRegistry(Keys.INGREDIENT, BurgerIngredient.DETACHED_CODEC)
    }

    object Keys {
        val INGREDIENT: RegistryKey<BurgerIngredient.ItemDetached> = ResourceKey.createRegistryKey(modLoc("ingredients"))
    }
}