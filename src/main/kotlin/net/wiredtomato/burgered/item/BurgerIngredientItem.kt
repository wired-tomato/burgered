package net.wiredtomato.burgered.item

import net.minecraft.component.DataComponentMap
import net.minecraft.component.DataComponentType
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.DirtyComponent
import java.util.*

open class BurgerIngredientItem(settings: BurgerIngredientSettings) : Item(settings), BurgerIngredient {
    private val saturation = settings.saturation()
    private val overSaturation = settings.overSaturation()
    private val modelHeight = settings.modelHeight()
    private val statusEffects = settings.statusEffects()

    override fun canBePutOn(stack: ItemStack, burger: Burger): Boolean {
        return true
    }

    override fun saturation(stack: ItemStack): Int = saturation
    override fun overSaturation(stack: ItemStack): Double = overSaturation
    override fun modelHeight(stack: ItemStack): Double = modelHeight
    override fun statusEffects(stack: ItemStack): List<StatusEffectEntry> = statusEffects
    override fun onEat(entity: LivingEntity, world: World, stack: ItemStack, component: FoodComponent) { }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val component = stack.getOrDefault(BurgeredDataComponents.DIRTY, DirtyComponent(true))
        if (component.dirty) {
            val effects = statusEffects(stack)
            val foodComponent = FoodComponent(
                saturation(stack),
                overSaturation(stack).toFloat(),
                effects.isNotEmpty(),
                0.5f,
                Optional.empty(),
                effects
            )

            stack.set(DataComponentTypes.FOOD, foodComponent)
            stack.set(BurgeredDataComponents.DIRTY, DirtyComponent(false))
        }
    }

    override fun getComponents(): DataComponentMap {
        val builder = DataComponentMap.builder()
        builder.putAll(super.getComponents())
        builder.put(BurgeredDataComponents.DIRTY, DirtyComponent(true))
        return builder.build()
    }

    class BurgerIngredientSettings : Settings() {
        private var saturation = 0
        private var overSaturation = 0.0
        private var modelHeight = 0.0
        private var statusEffects = mutableListOf<StatusEffectEntry>()

        fun saturation(): Int = saturation
        fun saturation(amount: Int): BurgerIngredientSettings {
            this.saturation = amount
            return this
        }

        fun overSaturation(): Double = overSaturation
        fun overSaturation(amount: Double): BurgerIngredientSettings {
            this.overSaturation = amount
            return this
        }

        fun modelHeight(): Double = modelHeight
        fun modelHeight(amount: Double): BurgerIngredientSettings {
            this.modelHeight = amount
            return this
        }

        fun statusEffects(): List<StatusEffectEntry> = statusEffects
        fun statusEffect(effect: StatusEffectInstance, probability: Float): BurgerIngredientSettings {
            statusEffects.add(StatusEffectEntry(effect, probability))
            return this
        }

        override fun <T : Any> component(type: DataComponentType<T>, value: T): BurgerIngredientSettings {
            super.component(type, value)
            return this
        }
    }
}