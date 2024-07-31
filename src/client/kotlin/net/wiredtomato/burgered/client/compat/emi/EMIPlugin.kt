package net.wiredtomato.burgered.client.compat.emi

import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import net.minecraft.recipe.RecipeType
import net.wiredtomato.burgered.recipe.VanillaBurgerIngredientRecipe

class EMIPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        val manager = registry.recipeManager
        manager.listAllOfType(RecipeType.SMITHING).forEach { recipe ->
            if (recipe.value is VanillaBurgerIngredientRecipe)
                registry.addRecipe(VanillaBurgerIngredientEMIRecipe(recipe))
        }
    }
}