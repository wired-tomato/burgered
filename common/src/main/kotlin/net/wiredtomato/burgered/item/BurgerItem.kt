package net.wiredtomato.burgered.item

import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.TooltipFlag
import net.minecraft.world.item.UseAnim
import net.minecraft.world.level.Level
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.BurgerComponent
import java.util.*

class BurgerItem(properties: Properties) : Item(properties) {
    override fun appendHoverText(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Component>,
        flag: TooltipFlag
    ) {
        stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent()).addToTooltip(context, { tooltip.add(it) }, flag)
    }

    override fun inventoryTick(stack: ItemStack, world: Level, entity: Entity, slot: Int, selected: Boolean) {
        val component = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        if (component.isDirty()) {
            val hunger = component.saturation()
            val saturation = component.overSaturation()
            val statusEffects = component.statusEffects()
            val eatTime = component.eatTime()

            val foodComponent = FoodProperties(
                hunger,
                saturation.toFloat(),
                statusEffects.isNotEmpty(),
                eatTime,
                Optional.empty(),
                statusEffects
            )

            stack.set(DataComponents.FOOD, foodComponent)
            stack.set(BurgeredDataComponents.BURGER, BurgerComponent(component.ingredients(), component.sloppiness(), false))
        }
    }

    override fun components(): DataComponentMap {
        val builder = DataComponentMap.builder()
        builder.addAll(super.components())
        builder.set(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)

        return builder.build()
    }

    override fun getUseAnimation(itemStack: ItemStack): UseAnim {
        return UseAnim.EAT
    }
}
