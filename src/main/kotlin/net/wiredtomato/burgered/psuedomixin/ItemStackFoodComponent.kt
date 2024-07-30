package net.wiredtomato.burgered.psuedomixin

import net.minecraft.component.DataComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.FoodComponent
import net.minecraft.item.ItemStack
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.init.BurgeredDataComponents
import java.util.*

fun modifyFoodComponent(stack: ItemStack, components: DataComponentMap): DataComponentMap {
    if (!components.contains(BurgeredDataComponents.BURGER)) return modifyFoodComponentIngredient(stack, components)
    val burger = components.get(BurgeredDataComponents.BURGER) ?: return components
    if (burger.ingredients().isEmpty()) return components

    val builder = DataComponentMap.builder()
    builder.putAll(components)

    val hunger = burger.saturation()
    val saturation = burger.overSaturation()
    val statusEffects = burger.statusEffects()
    val eatTime = burger.eatTime()

    val foodComponent = FoodComponent(
        hunger,
        saturation.toFloat(),
        statusEffects.isNotEmpty(),
        eatTime,
        Optional.empty(),
        statusEffects
    )

    builder.put(DataComponentTypes.FOOD, foodComponent)

    return builder.build()
}

fun modifyFoodComponentIngredient(stack: ItemStack, components: DataComponentMap): DataComponentMap {
    val item = stack.item
    if (item !is BurgerIngredient) return components

    val builder = DataComponentMap.builder()
    builder.putAll(components)

    val hunger = item.saturation()
    val saturation = item.overSaturation()
    val statusEffects = item.statusEffects()
    val eatTime = 0.5f

    val foodComponent = FoodComponent(
        hunger,
        saturation.toFloat(),
        statusEffects.isNotEmpty(),
        eatTime,
        Optional.empty(),
        statusEffects
    )

    builder.put(DataComponentTypes.FOOD, foodComponent)

    return builder.build()
}
