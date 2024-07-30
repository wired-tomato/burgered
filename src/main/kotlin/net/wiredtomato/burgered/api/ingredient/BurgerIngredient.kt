package net.wiredtomato.burgered.api.ingredient

import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry

interface BurgerIngredient : ItemConvertible {
    fun canBePutOn(stack: ItemStack, burger: Burger): Boolean
    fun saturation(): Int
    fun overSaturation(): Double
    fun modelHeight(): Double
    fun statusEffects(): List<StatusEffectEntry>
}
