package net.wiredtomato.burgered.init

import dev.architectury.registry.registries.Registrar
import dev.architectury.registry.registries.RegistrarManager
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.data.burger.BurgerStackableEatCallback

object BurgeredRegistries {
    val MANAGER by lazy { RegistrarManager.get(Burgered.MOD_ID) }
    val EAT_EVENT: Registrar<BurgerStackableEatCallback> = MANAGER.builder(Burgered.modLoc("eat_events"), *arrayOf<BurgerStackableEatCallback>()).build()
}
