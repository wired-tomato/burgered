package net.wiredtomato.burgered.api.burger

import net.minecraft.network.chat.Component
import net.minecraft.world.item.ItemStack
import net.wiredtomato.burgered.api.ConsumptionEffect
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredient
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredientInstance

interface Burger {
    fun ingredients(): List<BurgerIngredientInstance>
    fun saturation(): Int
    fun overSaturation(): Double
    fun consumptionEffects(): List<ConsumptionEffect>
    fun eatTime(): Float
    fun sloppiness(): Double

    interface Modifier<T : Burger> {
        fun setSloppiness(burger: T, stack: ItemStack, sloppiness: Double)
        fun appendIngredient(burger: T, stack: ItemStack, ingredientStack: ItemStack, ingredient: BurgerIngredient): Component?
        fun removeIngredient(burger: T, stack: ItemStack, ingredientStack: ItemStack, ingredient: BurgerIngredient)
        fun removeLastIngredient(burger: T, stack: ItemStack)
    }
}
