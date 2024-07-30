package net.wiredtomato.burgered.item.components

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.TooltipAppender
import net.minecraft.registry.Registries
import net.minecraft.text.Text
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.data.gen.CommonText
import net.wiredtomato.burgered.init.BurgeredDataComponents
import java.util.function.Consumer

data class BurgerComponent(
    private val burgerIngredients: List<BurgerIngredient> = listOf(),
    private val burgerSloppiness: Double = 0.0,
) : Burger, TooltipAppender {

    override fun appendToTooltip(
        context: Item.TooltipContext,
        appender: Consumer<Text>,
        config: TooltipConfig
    ) {
        appender.accept(Text.translatable(CommonText.INGREDIENTS))
        burgerIngredients.forEach { ingredient ->
            appender.accept(ingredient.asItem().name)
        }
    }

    override fun ingredients(): List<BurgerIngredient> = burgerIngredients

    override fun saturation(): Int {
        return burgerIngredients.map { it.saturation() }.reduce { acc, d -> acc + d }
    }

    override fun overSaturation(): Double {
        return burgerIngredients.map { it.overSaturation() }.reduce { acc, d -> acc + d }
    }

    override fun statusEffects(): List<StatusEffectEntry> {
        return ingredients().map { it.statusEffects() }.flatten()
    }

    override fun eatTime(): Float {
        return ingredients().size / 2f
    }

    override fun sloppiness(): Double = burgerSloppiness

    companion object : Burger.Modifier<BurgerComponent> {
        val INGREDIENT_CODEC: Codec<BurgerIngredient> = Registries.ITEM.codec.xmap(::fromItem, ::toItem)
        val CODEC: Codec<BurgerComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                INGREDIENT_CODEC.listOf().fieldOf("ingredients").forGetter(BurgerComponent::burgerIngredients),
                Codec.DOUBLE.fieldOf("sloppiness").orElse(0.0).forGetter(BurgerComponent::burgerSloppiness),
            ).apply(builder, ::BurgerComponent)
        }

        val DEFAULT = BurgerComponent()

        fun fromItem(ingredient: Item): BurgerIngredient {
            if (ingredient !is BurgerIngredient) throw IllegalStateException("Non ingredient item found: $ingredient")
            return ingredient
        }

        fun toItem(ingredient: BurgerIngredient): Item = ingredient.asItem()

        override fun appendIngredient(burger: BurgerComponent, stack: ItemStack, ingredient: BurgerIngredient): Option<Text> {
            val ingredients = burger.ingredients().toMutableList()

            val result = if (ingredient.canBePutOn(stack, burger)) {
                ingredients.add(ingredient)
                none()
            } else Text.translatable(CommonText.CANT_BE_PUT_ON_BURGER, ingredient.asItem().name).some()

            stack.set(BurgeredDataComponents.BURGER, BurgerComponent(ingredients, burger.sloppiness()))

            return result
        }

        override fun removeIngredient(burger: BurgerComponent, stack: ItemStack, ingredient: BurgerIngredient) {
            val ingredients = burger.ingredients().toMutableList()
            ingredients.remove(ingredient)
            stack.set(BurgeredDataComponents.BURGER, BurgerComponent(ingredients, burger.sloppiness()))
        }

        override fun removeLastIngredient(burger: BurgerComponent, stack: ItemStack) {
            val ingredients = burger.ingredients().toMutableList()
            if (ingredients.isEmpty()) return
            ingredients.removeLast()
            stack.set(BurgeredDataComponents.BURGER, BurgerComponent(ingredients, burger.sloppiness()))
        }

        override fun setSloppiness(burger: BurgerComponent, stack: ItemStack, sloppiness: Double) {
            stack.set(BurgeredDataComponents.BURGER, BurgerComponent(burger.burgerIngredients.toMutableList(), sloppiness))
        }
    }
}