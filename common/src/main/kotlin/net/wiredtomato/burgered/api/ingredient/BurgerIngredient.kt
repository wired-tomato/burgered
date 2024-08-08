package net.wiredtomato.burgered.api.ingredient

import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.event.LivingEntityEvents

interface BurgerIngredient : ItemLike, LivingEntityEvents.EatCallback {
    fun canBePutOn(stack: ItemStack, burger: Burger): Boolean
    fun saturation(stack: ItemStack): Int
    fun overSaturation(stack: ItemStack): Double
    fun modelHeight(stack: ItemStack): Double
    fun statusEffects(stack: ItemStack): List<StatusEffectEntry>
}
