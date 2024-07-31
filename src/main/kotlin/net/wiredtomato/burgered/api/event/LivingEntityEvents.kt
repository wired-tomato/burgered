package net.wiredtomato.burgered.api.event

import net.fabricmc.fabric.api.event.Event
import net.fabricmc.fabric.api.event.EventFactory
import net.minecraft.entity.LivingEntity
import net.minecraft.item.FoodComponent
import net.minecraft.item.ItemStack
import net.minecraft.world.World
import net.wiredtomato.burgered.api.event.LivingEntityEvents.EatCallback

object LivingEntityEvents {
    val ON_EAT: Event<EatCallback> = EventFactory.createArrayBacked(EatCallback::class.java) { listeners ->
        EatCallback { entity, world, stack, component ->
            listeners.forEach { it.onEat(entity, world, stack, component) }
        }
    }

    fun interface EatCallback {
        fun onEat(entity: LivingEntity, world: World, stack: ItemStack, component: FoodComponent)
    }
}