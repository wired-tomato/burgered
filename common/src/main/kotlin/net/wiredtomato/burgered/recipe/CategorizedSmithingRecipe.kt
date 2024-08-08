package net.wiredtomato.burgered.recipe

import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.crafting.SmithingRecipe

interface CategorizedSmithingRecipe : SmithingRecipe {
    fun category(): RecipeCategory
}
