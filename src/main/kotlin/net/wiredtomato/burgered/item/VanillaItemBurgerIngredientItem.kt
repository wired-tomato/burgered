package net.wiredtomato.burgered.item

import net.minecraft.block.AbstractPlantBlock
import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.BlockItem
import net.minecraft.item.FoodComponent
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.data.burger.BurgerStackable
import net.wiredtomato.burgered.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent
import java.util.*

class VanillaItemBurgerIngredientItem(settings: BurgerIngredientSettings) : BurgerIngredientItem(settings) {
    override fun saturation(stack: ItemStack): Int {
        return findFirstMatchingVanillaItem(stack).hunger
    }

    override fun overSaturation(stack: ItemStack): Double {
        return findFirstMatchingVanillaItem(stack).saturation.toDouble()
    }

    override fun statusEffects(stack: ItemStack): List<StatusEffectEntry> {
        return findFirstMatchingVanillaItem(stack).statusEffects
    }

    override fun modelHeight(stack: ItemStack): Double {
        return findFirstMatchingVanillaItem(stack).modelHeight
    }

    fun shouldApplyBlockTransformations(stack: ItemStack): Boolean {
        val vanillaStack = getVanillaStack(stack)
        val item = vanillaStack.item

        return findFirstMatchingVanillaItem(stack).modelHeight == 16.0 || (item is BlockItem && item.block !is AbstractPlantBlock)
    }

    override fun onEat(entity: LivingEntity, world: World, stack: ItemStack, component: FoodComponent) {
        findFirstMatchingVanillaItem(stack).eatEvent.ifPresent { it.onEat(entity, world, stack, component) }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
        val component = stack.getOrDefault(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT)
        if (component.isDirty()) {
            val effects = statusEffects(stack)
            stack.set(
                DataComponentTypes.FOOD,
                FoodComponent(
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

    fun findFirstMatchingVanillaItem(stack: ItemStack): BurgerStackable {
        val vanillaItem = getVanillaStack(stack)
        return if (vanillaItem.isOf(BurgeredItems.CUSTOM_BURGER_INGREDIENT)) {
            BurgerStackable(BurgeredItems.CUSTOM_BURGER_INGREDIENT, 0, 0f)
        } else BurgerStackables.find { it.item == vanillaItem.item }!!
    }

    override fun getName(stack: ItemStack): Text {
        val option: Optional<Text> = findFirstMatchingVanillaItem(stack).customName.map { Text.translatable(it) }
        return option.orElse(getVanillaStack(stack).name)
    }

    fun getVanillaStack(stack: ItemStack): ItemStack {
        return stack.getOrDefault(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT).stack
    }
}
