package net.wiredtomato.burgered.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.*
import net.wiredtomato.burgered.init.BurgeredRecipes

class GrillingRecipe(
    group: String,
    category: CookingCategory,
    ingredient: Ingredient,
    val result: ItemStack,
    experience: Float,
    cookTime: Int
) : AbstractCookingRecipe(BurgeredRecipes.GRILLING, group, category, ingredient, result, experience, cookTime) {
    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    companion object {
        val SERIALIZER = CookingRecipeSerializer(::GrillingRecipe, 100)
    }
}