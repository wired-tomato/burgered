package net.wiredtomato.burgered.api.event

import dev.architectury.event.Event
import dev.architectury.event.EventFactory
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.food.FoodProperties
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level

object LivingEntityEvents {
    val ON_EAT: Event<EatCallback> = EventFactory.createLoop(EatCallback::class.java)

    fun interface EatCallback {
        fun onEat(entity: LivingEntity, world: Level, stack: ItemStack, component: FoodProperties)
    }
}