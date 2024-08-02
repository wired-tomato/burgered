package net.wiredtomato.burgered.client.compat.emi

import dev.emi.emi.api.recipe.BasicEmiRecipe
import dev.emi.emi.api.render.EmiTexture
import dev.emi.emi.api.stack.EmiIngredient
import dev.emi.emi.api.stack.EmiStack
import dev.emi.emi.api.widget.WidgetHolder
import net.minecraft.recipe.RecipeHolder
import net.wiredtomato.burgered.recipe.GrillingRecipe

class GrillingEmiRecipe(
    holder: RecipeHolder<GrillingRecipe>
) : BasicEmiRecipe(EMIPlugin.GRILLING_RECIPE_CATEGORY, holder.id, 112, 18) {
    init {
        inputs.add(EmiIngredient.of(holder.value.ingredients.first()))
        outputs.add(EmiStack.of(holder.value.result))
    }

    override fun addWidgets(widgets: WidgetHolder) {
        widgets.addTexture(EmiTexture.EMPTY_ARROW, 44, 1)
        widgets.addSlot(inputs.first(), 18, 0)
        widgets.addSlot(outputs.first(), 76, 0).recipeContext(this)
    }
}