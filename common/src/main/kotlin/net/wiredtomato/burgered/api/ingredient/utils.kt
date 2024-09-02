package net.wiredtomato.burgered.api.ingredient

import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.data.burger.BurgerStackables
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings

fun Item.ingredient(): BurgerIngredient? {
    if (this is BurgerIngredient) return this

    if (BurgerStackables.map { it.item }.contains(this)) {
        val stackable = BurgerStackables.find { it.item == this } ?: return null
        return object : BurgerIngredient {
            override fun canBePutOn(ingredientStack: ItemStack, burger: Burger, burgerStack: ItemStack): Boolean = true
            override fun saturation(instance: BurgerIngredientInstance): Int = stackable.hunger
            override fun overSaturation(instance: BurgerIngredientInstance): Double = stackable.saturation.toDouble()
            override fun statusEffects(instance: BurgerIngredientInstance): List<StatusEffectEntry> = stackable.statusEffects
            override fun renderSettings(instance: BurgerIngredientInstance): IngredientRenderSettings = stackable.renderSettings
            override fun asItem(): Item = this@ingredient
            override fun onEat(entity: LivingEntity, world: Level, stack: ItemStack, component: FoodProperties) { }

            override fun equals(other: Any?): Boolean {
                if (other == null) return false
                if (other !is BurgerIngredient) return false
                if (this === other) return true
                if (this.asItem() == other.asItem()) return true

                return false
            }
        }
    }

    return null
}
