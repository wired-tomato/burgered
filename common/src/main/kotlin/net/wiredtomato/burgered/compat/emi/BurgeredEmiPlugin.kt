package net.wiredtomato.burgered.compat.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.world.item.crafting.Ingredient
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredBlocks
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.init.BurgeredRecipes

@EmiEntrypoint
class BurgeredEmiPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        registry.addCategory(GRILLING_RECIPE_CATEGORY)
        registry.addWorkstation(GRILLING_RECIPE_CATEGORY, EmiIngredient.of(Ingredient.of(BurgeredBlocks.GRILL)))
        registerStacks(registry)

        val manager = registry.recipeManager
        manager.getAllRecipesFor(BurgeredRecipes.GRILLING).forEach { recipe ->
            registry.addRecipe(GrillingEmiRecipe(recipe))
        }
    }

    fun registerStacks(registry: EmiRegistry) {}

    companion object {
        val GRILLING_ICON = EmiStack.of(BurgeredItems.GRILL)
        val GRILLING_RECIPE_CATEGORY = EmiRecipeCategory(Burgered.modLoc("grilling"), GRILLING_ICON, GRILLING_ICON)
    }
}