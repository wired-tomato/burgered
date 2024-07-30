package net.wiredtomato.burgered.psuedomixin

import net.minecraft.component.DataComponentMap
import net.minecraft.component.DataComponentTypes
import net.minecraft.item.FoodComponent
import net.wiredtomato.burgered.init.BurgeredDataComponents
import java.util.*

fun modifyFoodComponent(components: DataComponentMap): DataComponentMap {
    if (!components.contains(BurgeredDataComponents.BURGER)) return components
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
