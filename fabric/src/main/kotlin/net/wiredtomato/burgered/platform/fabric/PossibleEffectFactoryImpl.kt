package net.wiredtomato.burgered.platform.fabric

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties

object PossibleEffectFactoryImpl {
    @JvmStatic
    fun createEffect(instance: MobEffectInstance, probability: Float): FoodProperties.PossibleEffect {
        return FoodProperties.PossibleEffect(instance, probability)
    }
}
