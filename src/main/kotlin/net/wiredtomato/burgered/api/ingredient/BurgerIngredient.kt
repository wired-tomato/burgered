package net.wiredtomato.burgered.api.ingredient

import net.minecraft.item.ItemConvertible
import net.minecraft.item.ItemStack
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.event.LivingEntityEvents

interface BurgerIngredient : ItemConvertible, LivingEntityEvents.EatCallback {
    fun canBePutOn(stack: ItemStack, burger: Burger): Boolean
    fun saturation(): Int
    fun overSaturation(): Double
    fun modelHeight(): Double
    fun statusEffects(): List<StatusEffectEntry>
}
