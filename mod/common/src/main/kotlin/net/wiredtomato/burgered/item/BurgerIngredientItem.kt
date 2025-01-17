package net.wiredtomato.burgered.item

import net.minecraft.core.component.DataComponentType
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.component.Consumable
import net.minecraft.world.item.consume_effects.ApplyStatusEffectsConsumeEffect
import net.wiredtomato.burgered.api.ConsumptionEffect
import net.wiredtomato.burgered.api.burger.Burger
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredient
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredientInstance
import net.wiredtomato.burgered.api.burger.ingredient.IngredientQuality
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings
import net.wiredtomato.burgered.init.BurgeredDataComponents
import org.joml.Vector3d

open class BurgerIngredientItem(properties: BurgerIngredientProperties) : Item(properties), BurgerIngredient {
    private val saturation = properties.hunger()
    private val overSaturation = properties.saturation()
    private val statusEffects = properties.consumptionEffects()
    private val renderSettings = properties.renderSettings()

    override fun appendHoverText(
        itemStack: ItemStack,
        tooltipContext: TooltipContext,
        list: MutableList<Component>,
        tooltipFlag: TooltipFlag
    ) {
        val quality = itemStack.get(BurgeredDataComponents.QUALITY)?.quality ?: IngredientQuality.NORMAL

        list.add(Component.literal("Quality: ").append(Component.translatable(quality.translationKey)))
    }

    override fun canBePutOn(ingredientStack: ItemStack, burger: Burger, burgerStack: ItemStack): Boolean {
        return true
    }

    override fun hunger(instance: BurgerIngredientInstance): Int = saturation
    override fun saturation(instance: BurgerIngredientInstance): Float = overSaturation
    override fun consumptionEffects(instance: BurgerIngredientInstance): List<ConsumptionEffect> = statusEffects
    override fun renderSettings(instance: BurgerIngredientInstance): IngredientRenderSettings = renderSettings

    class BurgerIngredientProperties : Properties() {
        private var hunger = 0
        private var saturation = 0f
        private var statusEffects = mutableListOf<ConsumptionEffect>()
        private var renderSettings: IngredientRenderSettings = IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 1.0)

        fun hunger(): Int = hunger
        fun hunger(amount: Int): BurgerIngredientProperties {
            this.hunger = amount
            return this
        }

        fun saturation(): Float = saturation
        fun saturation(amount: Float): BurgerIngredientProperties {
            this.saturation = amount
            return this
        }

        fun consumptionEffects(): List<ConsumptionEffect> = statusEffects
        fun consumptionEffect(effect: ConsumptionEffect): BurgerIngredientProperties {
            statusEffects.add(effect)
            return this
        }

        fun singleStatusEffect(statusEffect: MobEffectInstance, probability: Float): BurgerIngredientProperties {
            return consumptionEffect(ApplyStatusEffectsConsumeEffect(statusEffect, probability))
        }

        fun renderSettings(): IngredientRenderSettings = renderSettings
        fun renderSettings(settings: IngredientRenderSettings): BurgerIngredientProperties {
            renderSettings = settings
            return this
        }

        fun createFoodComponents(): BurgerIngredientProperties {
            val foodComponent = FoodProperties(
                hunger(),
                saturation().toFloat(),
                consumptionEffects().isNotEmpty()
            )

            val consumable = Consumable.builder()
                .consumeSeconds(0.5f)
                .also { builder ->
                    consumptionEffects().forEach(builder::onConsume)
                }.build()



            component(DataComponents.FOOD, foodComponent)
            component(DataComponents.CONSUMABLE, consumable)

            return this
        }

        override fun <T : Any> component(type: DataComponentType<T>, value: T): BurgerIngredientProperties {
            super.component(type, value)
            return this
        }
    }
}
