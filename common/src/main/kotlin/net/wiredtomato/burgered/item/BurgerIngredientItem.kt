package net.wiredtomato.burgered.item

import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.DirtyComponent
import net.wiredtomato.burgered.platform.PossibleEffectFactory
import java.util.*

open class BurgerIngredientItem(properties: BurgerIngredientProperties) : Item(properties), BurgerIngredient {
    private val saturation = properties.saturation()
    private val overSaturation = properties.overSaturation()
    private val modelHeight = properties.modelHeight()
    private val statusEffects = properties.statusEffects()

    override fun canBePutOn(stack: ItemStack, burger: Burger): Boolean {
        return true
    }

    override fun saturation(stack: ItemStack): Int = saturation
    override fun overSaturation(stack: ItemStack): Double = overSaturation
    override fun modelHeight(stack: ItemStack): Double = modelHeight
    override fun statusEffects(stack: ItemStack): List<StatusEffectEntry> = statusEffects
    override fun onEat(entity: LivingEntity, world: Level, stack: ItemStack, component: FoodProperties) { }

    override fun inventoryTick(stack: ItemStack, world: Level, entity: Entity, slot: Int, selected: Boolean) {
        val component = stack.getOrDefault(BurgeredDataComponents.DIRTY, DirtyComponent(true))
        if (component.dirty) {
            val effects = statusEffects(stack)
            val foodComponent = FoodProperties(
                saturation(stack),
                overSaturation(stack).toFloat(),
                effects.isNotEmpty(),
                0.5f,
                Optional.empty(),
                effects
            )

            stack.set(DataComponents.FOOD, foodComponent)
            stack.set(BurgeredDataComponents.DIRTY, DirtyComponent(false))
        }
    }


    override fun components(): DataComponentMap {
        val builder = DataComponentMap.builder()
        builder.addAll(super.components())
        builder.set(BurgeredDataComponents.DIRTY, DirtyComponent(true))
        return builder.build()
    }

    class BurgerIngredientProperties : Properties() {
        private var saturation = 0
        private var overSaturation = 0.0
        private var modelHeight = 0.0
        private var statusEffects = mutableListOf<StatusEffectEntry>()

        fun saturation(): Int = saturation
        fun saturation(amount: Int): BurgerIngredientProperties {
            this.saturation = amount
            return this
        }

        fun overSaturation(): Double = overSaturation
        fun overSaturation(amount: Double): BurgerIngredientProperties {
            this.overSaturation = amount
            return this
        }

        fun modelHeight(): Double = modelHeight
        fun modelHeight(amount: Double): BurgerIngredientProperties {
            this.modelHeight = amount
            return this
        }

        fun statusEffects(): List<StatusEffectEntry> = statusEffects
        fun statusEffect(effect: MobEffectInstance, probability: Float): BurgerIngredientProperties {
            statusEffects.add(PossibleEffectFactory.createEffect(effect, probability))
            return this
        }

        override fun <T : Any> component(type: DataComponentType<T>, value: T): BurgerIngredientProperties {
            super.component(type, value)
            return this
        }
    }
}
