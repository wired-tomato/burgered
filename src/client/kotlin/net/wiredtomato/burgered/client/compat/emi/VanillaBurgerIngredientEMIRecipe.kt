package net.wiredtomato.burgered.client.compat.emi

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeHolder
import net.minecraft.recipe.SmithingRecipe
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredItems

class VanillaBurgerIngredientEMIRecipe(
    val holder: RecipeHolder<SmithingRecipe>
) : BasicEmiRecipe(VanillaEmiRecipeCategories.SMITHING, Burgered.id("vanilla_burger_ingredient_emi_recipe"), 112, 18) {
    init {
        inputs.add(EmiIngredient.of(Ingredient.ofItems(BurgeredItems.BOOK_OF_BURGERS)))
        inputs.add(EmiIngredient.of(BurgerStackables.map { EmiIngredient.of(Ingredient.ofItems(it.item)) }))
        inputs.add(EmiIngredient.of(Ingredient.ofItems(BurgeredItems.BURGER)))
        outputs.add(EmiStack.of(BurgeredItems.CUSTOM_BURGER_INGREDIENT))
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1)
        widgets.addSlot(inputs.first(), 0, 0)
        widgets.addSlot(inputs[1], 18, 0)
        widgets.addSlot(inputs[2], 36, 0)
        widgets.addSlot(outputs.first(), 94, 0).recipeContext(this)
    }
}