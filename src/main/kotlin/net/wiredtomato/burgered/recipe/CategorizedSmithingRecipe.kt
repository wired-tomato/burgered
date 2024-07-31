package net.wiredtomato.burgered.recipe

import net.minecraft.recipe.RecipeCategory
import net.minecraft.recipe.SmithingRecipe

interface CategorizedSmithingRecipe : SmithingRecipe {
    fun category(): RecipeCategory
}