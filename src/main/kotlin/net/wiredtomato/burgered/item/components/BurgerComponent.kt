package net.wiredtomato.burgered.item.components

import arrow.core.Option
import arrow.core.none
import arrow.core.some
import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.client.item.TooltipConfig
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.TooltipAppender
import net.minecraft.text.Text
import net.minecraft.world.World
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.data.CommonText
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.util.createGroupsBy
import java.text.DecimalFormat
import java.util.function.Consumer

data class BurgerComponent(
    private val burgerIngredients: List<Pair<ItemStack, BurgerIngredient>> = listOf(),
    private val burgerSloppiness: Double = 0.0,
) : Burger, TooltipAppender {

    override fun appendToTooltip(
        context: Item.TooltipContext,
        appender: Consumer<Text>,
        config: TooltipConfig
    ) {
        val format = DecimalFormat("#.##")
        appender.accept(Text.translatable(CommonText.SLOPPINESS, format.format(burgerSloppiness)))
        appender.accept(Text.empty())

        appender.accept(Text.translatable(CommonText.INGREDIENTS))
        burgerIngredients.reversed().createGroupsBy { it.second }.forEach { group ->
            appender.accept(Text.literal("${group.count}x ").append(group.value.second.asItem().getName(group.value.first)))
        }
    }

    override fun ingredients(): List<Pair<ItemStack, BurgerIngredient>> = burgerIngredients

    override fun saturation(): Int {
        return burgerIngredients.map { it.second.saturation() }.reduce { acc, d -> acc + d }
    }

    override fun overSaturation(): Double {
        return burgerIngredients.map { it.second.overSaturation() }.reduce { acc, d -> acc + d }
    }

    override fun statusEffects(): List<StatusEffectEntry> {
        return ingredients().map { it.second.statusEffects() }.flatten()
    }

    override fun eatTime(): Float {
        return (ingredients().size / 2f).coerceAtMost(2f)
    }

    override fun sloppiness(): Double = burgerSloppiness
    override fun onEat(entity: LivingEntity, world: World, stack: ItemStack, component: FoodComponent) {
        ingredients().forEach { it.second.onEat(entity, world, stack, component) }
    }

    companion object : Burger.Modifier<BurgerComponent> {
        val INGREDIENT_PAIR_CODEC: Codec<Pair<ItemStack, BurgerIngredient>> = ItemStack.CODEC.xmap({ Pair(it, fromItem(it.item)) }, { it.first })
        val CODEC: Codec<BurgerComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                INGREDIENT_PAIR_CODEC.listOf().fieldOf("ingredients").forGetter(BurgerComponent::burgerIngredients),
                Codec.DOUBLE.fieldOf("sloppiness").orElse(0.0).forGetter(BurgerComponent::burgerSloppiness),
            ).apply(builder, ::BurgerComponent)
        }

        val DEFAULT = BurgerComponent(
            listOf(
                BurgeredItems.BOTTOM_BUN.defaultStack to BurgeredItems.BOTTOM_BUN,
                BurgeredItems.BEEF_PATTY.defaultStack to BurgeredItems.BEEF_PATTY,
                BurgeredItems.CHEESE_SLICE.defaultStack to BurgeredItems.CHEESE_SLICE,
                BurgeredItems.TOP_BUN.defaultStack to BurgeredItems.TOP_BUN
            )
        )

        fun fromItem(ingredient: Item): BurgerIngredient {
            if (ingredient !is BurgerIngredient) throw IllegalStateException("Non ingredient item found: $ingredient")
            return ingredient
        }

        override fun appendIngredient(burger: BurgerComponent, stack: ItemStack, ingredientStack: ItemStack, ingredient: BurgerIngredient): Option<Text> {
            val ingredients = burger.ingredients().toMutableList()
            if (ingredients.size >= 2048) return Text.translatable(CommonText.BURGER_MAX_SIZE).some()

            val result = if (ingredient.canBePutOn(stack, burger)) {
                ingredients.add(ingredientStack to ingredient)
                none()
            } else Text.translatable(CommonText.CANT_BE_PUT_ON_BURGER, ingredient.asItem().name).some()

            stack.set(BurgeredDataComponents.BURGER, BurgerComponent(ingredients, burger.sloppiness()))

            return result
        }

        override fun removeIngredient(burger: BurgerComponent, stack: ItemStack, ingredientStack: ItemStack, ingredient: BurgerIngredient) {
            val ingredients = burger.ingredients().toMutableList()
            ingredients.remove(ingredientStack to ingredient)
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