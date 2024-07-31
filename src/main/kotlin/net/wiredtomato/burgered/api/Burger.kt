package net.wiredtomato.burgered.api

import arrow.core.Option
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.wiredtomato.burgered.api.event.LivingEntityEvents
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient

interface Burger : LivingEntityEvents.EatCallback {
    fun ingredients(): List<Pair<ItemStack, BurgerIngredient>>
    fun saturation(): Int
    fun overSaturation(): Double
    fun statusEffects(): List<StatusEffectEntry>
    fun eatTime(): Float
    fun sloppiness(): Double

    interface Modifier<T : Burger> {
        fun setSloppiness(burger: T, stack: ItemStack, sloppiness: Double)
        fun appendIngredient(burger: T, stack: ItemStack, ingredientStack: ItemStack, ingredient: BurgerIngredient): Option<Text>
        fun removeIngredient(burger: T, stack: ItemStack, ingredientStack: ItemStack, ingredient: BurgerIngredient)
        fun removeLastIngredient(burger: T, stack: ItemStack)
    }
}
