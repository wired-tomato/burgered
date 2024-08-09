package net.wiredtomato.burgered.compat.emi

import dev.emi.emi.api.EmiEntrypoint
import dev.emi.emi.api.EmiPlugin
import dev.emi.emi.api.EmiRegistry
import dev.emi.emi.api.recipe.EmiRecipeCategory
import dev.emi.emi.api.stack.EmiStack
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeType
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.init.BurgeredRecipes
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent
import net.wiredtomato.burgered.recipe.VanillaBurgerIngredientRecipe

@EmiEntrypoint
class BurgeredEmiPlugin : EmiPlugin {
    override fun register(registry: EmiRegistry) {
        registry.addCategory(GRILLING_RECIPE_CATEGORY)
        registerStacks(registry)

        val manager = registry.recipeManager
        manager.getAllRecipesFor(RecipeType.SMITHING).forEach { recipe ->
            if (recipe.value is VanillaBurgerIngredientRecipe)
                registry.addRecipe(VanillaBurgerIngredientEMIRecipe(recipe))
        }

        manager.getAllRecipesFor(BurgeredRecipes.GRILLING).forEach { recipe ->
            registry.addRecipe(GrillingEmiRecipe(recipe))
        }
    }

    fun registerStacks(registry: EmiRegistry) {
        registry.removeEmiStacks { emiStack ->
            val stack = emiStack.itemStack
            if (stack.`is`(BurgeredItems.CUSTOM_BURGER_INGREDIENT)) return@removeEmiStacks true

            val component = stack.get(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT) ?: return@removeEmiStacks false

            component.stack.`is`(BurgeredItems.CUSTOM_BURGER_INGREDIENT)
        }

        var lastStack: ItemStack? = null
        BurgerStackables.forEach { stackable ->
            val vanillaIngredient = BurgeredItems.VANILLA_INGREDIENT.defaultInstance
            vanillaIngredient.set(
                BurgeredDataComponents.VANILLA_BURGER_INGREDIENT,
                VanillaBurgerIngredientComponent(stackable.item.defaultInstance)
            )

            val stack = EmiStack.of(vanillaIngredient)
            vanillaIngredientStacks.add(stack)
            registry.addEmiStackAfter(stack) { emiStack ->
                val lStack = lastStack
                if (lStack == null) {
                    return@addEmiStackAfter emiStack.itemStack.`is`(BurgeredItems.GRILL)
                }

                val iStack = emiStack.itemStack
                val current = iStack.get(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT) ?: return@addEmiStackAfter false
                val last = lStack.get(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT) ?: return@addEmiStackAfter false

                current.stack.`is`(last.stack.item)
            }
            lastStack = vanillaIngredient
        }
    }

    companion object {
        val GRILLING_ICON = EmiStack.of(BurgeredItems.GRILL)
        val GRILLING_RECIPE_CATEGORY = EmiRecipeCategory(Burgered.modLoc("grilling"), GRILLING_ICON, GRILLING_ICON)

        internal val vanillaIngredientStacks = mutableListOf<EmiStack>()
    }
}