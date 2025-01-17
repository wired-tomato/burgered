package net.wiredtomato.burgered.recipe

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.*
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.init.BurgeredRecipeBookCategories
import net.wiredtomato.burgered.init.BurgeredRecipes

class GrillingRecipe(
    group: String,
    category: CookingBookCategory,
    val ingredient: Ingredient,
    val transform: ItemStack,
    val result: ItemStack,
    experience: Float,
    cookTime: Int
) : AbstractCookingRecipe(group, category, ingredient, result, experience, cookTime) {
    override fun getSerializer(): RecipeSerializer<GrillingRecipe> = SERIALIZER
    override fun recipeBookCategory(): RecipeBookCategory = BurgeredRecipeBookCategories.GRILLING
    override fun getType(): RecipeType<GrillingRecipe> = BurgeredRecipes.GRILLING
    override fun furnaceIcon(): Item = BurgeredItems.GRILL

    companion object {
        val SERIALIZER = GrillingRecipeSerializer()
    }
}
