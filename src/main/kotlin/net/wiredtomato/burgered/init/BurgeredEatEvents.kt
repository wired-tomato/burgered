package net.wiredtomato.burgered.init

import net.minecraft.registry.Registry
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.data.burger.BurgerStackableEatCallback

object BurgeredEatEvents {
    val NONE = register("none", BurgerStackableEatCallback { _, _, _, _ ->  })

    fun <T : BurgerStackableEatCallback> register(path: String, callback: T) : T {
        return Registry.register(BurgeredRegistries.EAT_EVENT, Burgered.id(path), callback)
    }
}