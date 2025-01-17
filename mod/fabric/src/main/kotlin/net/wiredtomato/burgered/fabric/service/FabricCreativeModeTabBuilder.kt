package net.wiredtomato.burgered.fabric.service

import deplatformed.ServiceImpl
import net.minecraft.world.item.CreativeModeTab
import net.wiredtomato.burgered.service.CreativeModeTabBuilder

@ServiceImpl(CreativeModeTabBuilder::class)
class FabricCreativeModeTabBuilder : CreativeModeTabBuilder {
    override fun createBuilder(): CreativeModeTab.Builder {
        return CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0)
    }
}