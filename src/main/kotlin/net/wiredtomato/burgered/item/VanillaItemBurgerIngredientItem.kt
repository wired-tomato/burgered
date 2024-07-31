package net.wiredtomato.burgered.item

import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent

class VanillaItemBurgerIngredientItem(settings: BurgerIngredientSettings) : BurgerIngredientItem(settings) {
    override fun getName(stack: ItemStack): Text {
        return getVanillaItem(stack).name
    }

    fun getVanillaItem(stack: ItemStack): Item {
        return stack.getOrDefault(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT).item
    }
}
