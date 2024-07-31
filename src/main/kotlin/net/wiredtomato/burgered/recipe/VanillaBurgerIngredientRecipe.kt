package net.wiredtomato.burgered.recipe

import net.minecraft.item.ItemStack
import net.minecraft.recipe.RecipeCategory
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.SmithingRecipeInput
import net.minecraft.registry.HolderLookup
import net.minecraft.world.World
import net.wiredtomato.burgered.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.item.components.BurgerComponent
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent

class VanillaBurgerIngredientRecipe(
    val category: RecipeCategory
) : CategorizedSmithingRecipe {
    override fun matches(input: SmithingRecipeInput, world: World): Boolean {
        return matchesTemplateIngredient(input.template) &&
                matchesBaseIngredient(input.base) &&
                matchesAdditionIngredient(input.addition)
    }

    fun isDefaultBurger(stack: ItemStack): Boolean {
        val burger = stack.get(BurgeredDataComponents.BURGER) ?: return false
        val ingredients = burger.ingredients().map { it.second }
        val shouldMatch = BurgerComponent.DEFAULT.ingredients().map { it.second }

        return ingredients == shouldMatch
    }

    override fun craft(input: SmithingRecipeInput, provider: HolderLookup.Provider): ItemStack {
        val output = BurgeredItems.VANILLA_INGREDIENT.defaultStack
        output.set(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent(input.base.item))
        return output
    }

    override fun getResult(provider: HolderLookup.Provider) = ItemStack.EMPTY
    override fun category(): RecipeCategory = category

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun matchesTemplateIngredient(stack: ItemStack): Boolean {
        return stack.isOf(BurgeredItems.BOOK_OF_BURGERS)
    }

    override fun matchesBaseIngredient(stack: ItemStack): Boolean {
        return BurgerStackables.map { it.item }.contains(stack.item)
    }

    override fun matchesAdditionIngredient(stack: ItemStack): Boolean {
        return isDefaultBurger(stack)
    }

    companion object {
        val SERIALIZER = SpecialSmithingRecipeSerializer(::VanillaBurgerIngredientRecipe)
    }
}