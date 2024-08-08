package net.wiredtomato.burgered.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.food.FoodProperties

object PossibleEffectFactory {
    @JvmStatic
    @ExpectPlatform
    fun createEffect(instance: MobEffectInstance, probability: Float): FoodProperties.PossibleEffect {
        throw AssertionError()
    }
}
