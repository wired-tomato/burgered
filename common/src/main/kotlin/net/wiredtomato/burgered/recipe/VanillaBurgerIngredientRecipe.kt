package net.wiredtomato.burgered.recipe

import net.minecraft.core.HolderLookup
import net.minecraft.data.recipes.RecipeCategory
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.SmithingRecipeInput
import net.minecraft.world.level.Level
import net.wiredtomato.burgered.api.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.item.components.BurgerComponent
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent

class VanillaBurgerIngredientRecipe(
    val category: RecipeCategory
) : CategorizedSmithingRecipe {
    override fun matches(input: SmithingRecipeInput, world: Level): Boolean {
        return isTemplateIngredient(input.template) &&
                isBaseIngredient(input.base) &&
                isAdditionIngredient(input.addition)
    }

    fun isDefaultBurger(stack: ItemStack): Boolean {
        val burger = stack.get(BurgeredDataComponents.BURGER) ?: return false
        val ingredients = burger.ingredients().map { it.second }
        val shouldMatch = BurgerComponent.DEFAULT.ingredients().map { it.second }

        return ingredients == shouldMatch
    }

    override fun assemble(input: SmithingRecipeInput, provider: HolderLookup.Provider): ItemStack {
        val output = BurgeredItems.VANILLA_INGREDIENT.defaultInstance
        output.set(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent(input.base.copy()))
        return output
    }

    override fun getResultItem(provider: HolderLookup.Provider) = ItemStack.EMPTY
    override fun category(): RecipeCategory = category

    override fun getSerializer(): RecipeSerializer<*> = SERIALIZER

    override fun isTemplateIngredient(stack: ItemStack): Boolean {
        return stack.`is`(BurgeredItems.BOOK_OF_BURGERS)
    }

    override fun isBaseIngredient(stack: ItemStack): Boolean {
        return BurgerStackables.map { it.item }.contains(stack.item)
    }

    override fun isAdditionIngredient(stack: ItemStack): Boolean {
        return isDefaultBurger(stack)
    }

    companion object {
        val SERIALIZER = SpecialSmithingRecipeSerializer(::VanillaBurgerIngredientRecipe)
    }
}
