package net.wiredtomato.burgered.item.components

import net.minecraft.entity.Entity
import net.minecraft.item.ItemStack
import net.minecraft.world.World

interface TickingComponent {
    fun tick(stack: ItemStack, world: World, entity: Entity, slot: Int, selected: Boolean)
}