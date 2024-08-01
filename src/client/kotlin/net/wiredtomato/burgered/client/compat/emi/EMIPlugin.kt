package net.wiredtomato.burgered.client.compat.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.recipe.RecipeType
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.init.BurgeredRecipes
import net.wiredtomato.burgered.recipe.VanillaBurgerIngredientRecipe

class EMIPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        registry.addCategory(GRILLING_RECIPE_CATEGORY)

        val manager = registry.recipeManager
        manager.listAllOfType(RecipeType.SMITHING).forEach { recipe ->
            if (recipe.value is VanillaBurgerIngredientRecipe)
                registry.addRecipe(VanillaBurgerIngredientEMIRecipe(recipe))
        }

        manager.listAllOfType(BurgeredRecipes.GRILLING).forEach { recipe ->
            registry.addRecipe(GrillingEmiRecipe(recipe))
        }
    }

    companion object {
        val GRILLING_ICON = EmiStack.of(BurgeredItems.GRILL)
        val GRILLING_RECIPE_CATEGORY = EmiRecipeCategory(Burgered.id("grilling"), GRILLING_ICON, GRILLING_ICON)
    }
}