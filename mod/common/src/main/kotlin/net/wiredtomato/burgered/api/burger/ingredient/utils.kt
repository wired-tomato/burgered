package net.wiredtomato.burgered.api.burger.ingredient

import net.minecraft.world.item.Item
import net.minecraft.world.level.Level
import net.wiredtomato.burgered.api.registry.BurgeredRegistries

fun Item.ingredient(level: Level? = null): BurgerIngredient? {
    if (this is BurgerIngredient) return this
    if (level == null) return null

    val registries = level.registryAccess()
    val ingredientRegistry = registries.lookupOrThrow(BurgeredRegistries.Keys.INGREDIENT)
    val ingredient = ingredientRegistry.find { it.item == this }
    return ingredient
}
