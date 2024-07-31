package net.wiredtomato.burgered.item

import net.minecraft.component.DataComponentTypes
import net.minecraft.entity.Entity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.text.Text
import net.minecraft.world.World
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.data.burger.BurgerStackable
import net.wiredtomato.burgered.data.burger.BurgerStackables
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent
import java.util.Optional

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

    override fun onEat(entity: LivingEntity, world: World, stack: ItemStack, component: FoodComponent) {
        findFirstMatchingVanillaItem(stack).eatEvent.ifPresent { it.onEat(entity, world, stack, component) }
    }

    override fun inventoryTick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean) {
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
    }

    fun findFirstMatchingVanillaItem(stack: ItemStack): BurgerStackable {
        return BurgerStackables.find { it.item == getVanillaItem(stack) }!!
    }

    override fun getName(stack: ItemStack): Text {
        val option: Optional<Text> = findFirstMatchingVanillaItem(stack).customName.map { Text.translatable(it) }
        return option.orElse(getVanillaItem(stack).name)
    }

    fun getVanillaItem(stack: ItemStack): Item {
        return stack.getOrDefault(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT).item
    }
}
