package net.wiredtomato.burgered.item

import net.minecraft.client.item.TooltipConfig
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.UseAction
import net.minecraft.world.World
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.BurgerComponent
import java.util.*

class BurgerItem(settings: Settings) : Item(settings) {
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        config: TooltipConfig
    ) {
        stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent()).appendToTooltip(context, { tooltip.add(it) }, config)
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val component = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        if (component.isDirty()) {
            val hunger = component.saturation()
            val saturation = component.overSaturation()
            val statusEffects = component.statusEffects()
            val eatTime = component.eatTime()

            val foodComponent = FoodComponent(
                hunger,
                saturation.toFloat(),
                statusEffects.isNotEmpty(),
                eatTime,
                Optional.empty(),
                statusEffects
            )

            stack.set(DataComponentTypes.FOOD, foodComponent)
            stack.set(BurgeredDataComponents.BURGER, BurgerComponent(component.ingredients(), component.sloppiness(), false))
        }
    }

    override fun getUseAction(stack: ItemStack): UseAction {
        return UseAction.EAT
    }
}