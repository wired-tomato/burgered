package net.wiredtomato.burgered.init

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.data.burger.BurgerStackableEatCallback

object BurgeredRegistries {
    val EAT_EVENT: Registry<BurgerStackableEatCallback> = FabricRegistryBuilder.createSimple(Keys.EAT_EVENT).buildAndRegister()

    object Keys {
        val EAT_EVENT: RegistryKey<Registry<BurgerStackableEatCallback>> = RegistryKey.ofRegistry(Burgered.id("eat_events"))
    }
}