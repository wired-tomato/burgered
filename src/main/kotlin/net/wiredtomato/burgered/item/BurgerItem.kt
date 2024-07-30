package net.wiredtomato.burgered.item

import net.minecraft.client.item.TooltipConfig
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.util.UseAction
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.BurgerComponent

class BurgerItem(settings: Settings) : Item(settings) {
    override fun appendTooltip(
        stack: ItemStack,
        context: TooltipContext,
        tooltip: MutableList<Text>,
        config: TooltipConfig
    ) {
        stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent()).appendToTooltip(context, { tooltip.add(it) }, config)
    }

    override fun getUseAction(stack: ItemStack): UseAction {
        return UseAction.EAT
    }
}