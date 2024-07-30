package net.wiredtomato.burgered.item

import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient

class BurgerIngredientItem(settings: BurgerIngredientSettings) : Item(settings), BurgerIngredient {
    private val saturation = settings.saturation()
    private val overSaturation = settings.overSaturation()
    private val expirationTime = settings.expirationTime()
    private val modelHeight = settings.modelHeight()
    private val statusEffects = settings.statusEffects()

    override fun canBePutOn(stack: ItemStack, burger: Burger): Boolean {
        return true
    }

    override fun saturation(): Int = saturation
    override fun overSaturation(): Double = overSaturation
    override fun expirationTime(): Int = expirationTime
    override fun modelHeight(): Double = modelHeight
    override fun statusEffects(): List<StatusEffectEntry> = statusEffects

    class BurgerIngredientSettings : Settings() {
        private var saturation = 0
        private var overSaturation = 0.0
        private var expirationTime = 0
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

        fun expirationTime(): Int = expirationTime
        fun expirationTime(amount: Int): BurgerIngredientSettings {
            this.expirationTime = amount
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
    }
}