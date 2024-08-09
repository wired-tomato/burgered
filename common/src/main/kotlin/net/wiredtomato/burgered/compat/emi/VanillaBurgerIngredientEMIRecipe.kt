package net.wiredtomato.burgered.compat.emi

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.recipe.VanillaEmiRecipeCategories
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SmithingRecipe
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredItems

class VanillaBurgerIngredientEMIRecipe(
    val holder: RecipeHolder<SmithingRecipe>
) : BasicEmiRecipe(VanillaEmiRecipeCategories.SMITHING, holder.id, 112, 18) {
    init {
        inputs.add(EmiIngredient.of(Ingredient.of(BurgeredItems.BOOK_OF_BURGERS)))
        inputs.add(EmiIngredient.of(BurgerStackables.map { EmiIngredient.of(Ingredient.of(it.item)) }))
        inputs.add(EmiIngredient.of(Ingredient.of(BurgeredItems.BURGER)))
        Burgered.LOGGER.info("${BurgeredEmiPlugin.vanillaIngredientStacks}")
        outputs.add(BurgeredEmiPlugin.vanillaIngredientStacks.first())
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 62, 1)
        widgets.addSlot(inputs.first(), 0, 0)
        widgets.addSlot(inputs[1], 18, 0)
        widgets.addSlot(inputs[2], 36, 0)
        widgets.addSlot(outputs.first(), 94, 0).recipeContext(this)
    }
}