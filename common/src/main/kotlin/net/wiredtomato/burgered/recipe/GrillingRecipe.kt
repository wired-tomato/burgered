package net.wiredtomato.burgered.recipe

import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.wiredtomato.burgered.init.BurgeredRecipes

class GrillingRecipe(
    group: String,
    category: CookingBookCategory,
    ingredient: Ingredient,
    val result: ItemStack,
    experience: Float,
    cookTime: Int
) : AbstractCookingRecipe(BurgeredRecipes.GRILLING, group, category, ingredient, result, experience, cookTime) {
    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = SimpleCookingSerializer(::GrillingRecipe, 100)
    }
}
