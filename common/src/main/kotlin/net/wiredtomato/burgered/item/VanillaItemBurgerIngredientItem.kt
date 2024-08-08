package net.wiredtomato.burgered.item

import net.minecraft.core.component.DataComponentMap
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BushBlock
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.data.burger.BurgerStackable
import net.wiredtomato.burgered.api.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent
import java.util.*

class VanillaItemBurgerIngredientItem(settings: BurgerIngredientProperties) : BurgerIngredientItem(settings) {
    override fun saturation(stack: ItemStack): Int {
        return findFirstMatchingVanillaItem(stack).hunger
    }

    override fun overSaturation(stack: ItemStack): Double {
        return findFirstMatchingVanillaItem(stack).saturation.toDouble()
    }

    override fun statusEffects(stack: ItemStack): List<StatusEffectEntry> {
        return findFirstMatchingVanillaItem(stack).statusEffects
    }

    fun shouldApplyBlockTransformations(stack: ItemStack): Boolean {
        val vanillaStack = getVanillaStack(stack)
        val item = vanillaStack.item

        return findFirstMatchingVanillaItem(stack).modelHeight == 16.0 || (item is BlockItem && item.block !is BushBlock)
    }

    override fun onEat(entity: LivingEntity, world: Level, stack: ItemStack, component: FoodProperties) {
        findFirstMatchingVanillaItem(stack).eatEvent.ifPresent { it.onEat(entity, world, stack, component) }
    }

    override fun inventoryTick(stack: ItemStack, world: Level, entity: Entity, slot: Int, selected: Boolean) {
        val component = stack.getOrDefault(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT)
        if (component.isDirty()) {
            val effects = statusEffects(stack)
            stack.set(
                DataComponents.FOOD,
                FoodProperties(
                    saturation(stack),
                    overSaturation(stack).toFloat(),
                    effects.isNotEmpty(),
                    0.5f,
                    Optional.empty(),
                    effects,
                )
            )

            stack.set(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent(component.stack, false))
        }
    }

    override fun components(): DataComponentMap {
        val builder = DataComponentMap.builder()
        builder.addAll(super.components())
        builder.set(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT)

        return builder.build()
    }

    fun findFirstMatchingVanillaItem(stack: ItemStack): BurgerStackable {
        val vanillaItem = getVanillaStack(stack)
        return if (vanillaItem.`is`(BurgeredItems.CUSTOM_BURGER_INGREDIENT)) {
            BurgerStackable(BurgeredItems.CUSTOM_BURGER_INGREDIENT, 0, 0f)
        } else BurgerStackables.find { it.item == vanillaItem.item }!!
    }

    override fun getName(stack: ItemStack): Component {
        val option: Optional<Component> = findFirstMatchingVanillaItem(stack).customName.map { Component.translatable(it) }
        return option.orElse(getVanillaStack(stack).displayName)
    }

    fun getVanillaStack(stack: ItemStack): ItemStack {
        return stack.getOrDefault(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT).stack
    }
}
