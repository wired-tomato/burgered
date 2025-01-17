package net.wiredtomato.burgered.fabric

import net.fabricmc.api.ModInitializer
import net.wiredtomato.burgered.Burgered

object FabricBurgered : ModInitializer {
    override fun onInitialize() {
        Burgered.commonInit()
    }
}