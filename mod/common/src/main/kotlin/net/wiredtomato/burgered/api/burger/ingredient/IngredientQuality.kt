package net.wiredtomato.burgered.api.burger.ingredient

import net.minecraft.core.component.DataComponents
import net.minecraft.util.StringRepresentable
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.Consumable
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredientInstance.Companion.instanceFromStack
import net.wiredtomato.burgered.api.data.component.IngredientQualityComponent
import net.wiredtomato.burgered.init.BurgeredDataComponents
import kotlin.math.roundToInt

enum class IngredientQuality(val translationKey: String, val multiplier: Double) : StringRepresentable {
    VERY_POOR("${Burgered.MOD_ID}.ingredient_quality.very_poor", 0.5),
    POOR("${Burgered.MOD_ID}.ingredient_quality.poor", 0.75),
    NORMAL("${Burgered.MOD_ID}.ingredient_quality.normal", 1.0),
    GOOD("${Burgered.MOD_ID}.ingredient_quality.good", 1.25),
    EXCELLENT("${Burgered.MOD_ID}.ingredient_quality.excellent", 1.5);

    override fun getSerializedName(): String {
        return name
    }

    fun nextUp(): IngredientQuality {
        return when (this) {
            EXCELLENT -> EXCELLENT
            GOOD -> EXCELLENT
            NORMAL -> GOOD
            POOR -> NORMAL
            VERY_POOR -> POOR
        }
    }

    fun nextDown(): IngredientQuality {
        return when (this) {
            EXCELLENT -> GOOD
            GOOD -> NORMAL
            NORMAL -> POOR
            POOR -> VERY_POOR
            VERY_POOR -> VERY_POOR
        }
    }

    companion object {
        val CODEC = StringRepresentable.fromEnum { IngredientQuality.entries.toTypedArray() }

        fun ItemStack.defaultQuality(): IngredientQuality {
            val item = this.item
            return if (item is BurgerIngredient) item.defaultQuality() else NORMAL
        }

        fun ItemStack.quality(): IngredientQuality {
            val item = this.item
            if (item !is BurgerIngredient) return NORMAL

            return get(BurgeredDataComponents.QUALITY)?.quality ?: item.defaultQuality()
        }

        fun ItemStack.withQuality(quality: IngredientQuality): ItemStack {
            val item = this.item
            if (item !is BurgerIngredient) return this
            val instance = item.instanceFromStack(this)
            val consumptionEffects = item.consumptionEffects(instance)
            val foodComponent = FoodProperties(
                (item.hunger(instance) * quality.multiplier).roundToInt(),
                (item.saturation(instance) * quality.multiplier).toFloat(),
                consumptionEffects.isNotEmpty()
            )

            val consumable = Consumable.builder()
                .consumeSeconds(0.5f)
                .also { builder ->
                    consumptionEffects.forEach(builder::onConsume)
                }.build()

            set(BurgeredDataComponents.QUALITY, IngredientQualityComponent(quality))
            set(DataComponents.FOOD, foodComponent)
            set(DataComponents.CONSUMABLE, consumable)

            return this
        }
    }
}
