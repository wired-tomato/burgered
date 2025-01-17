package net.wiredtomato.burgered.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.RecipeBookCategory
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.service.RegistryServiceImpl

object BurgeredRecipeBookCategories {
    val GRILLING by registering("grilling")

    fun registering(path: String): KSupplier<RecipeBookCategory> {
        return RegistryServiceImpl.register(BuiltInRegistries.RECIPE_BOOK_CATEGORY, Burgered.modLoc(path), ::RecipeBookCategory)
    }
}