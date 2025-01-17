package net.wiredtomato.burgered.neoforge.service

import deplatformed.ServiceImpl
import net.minecraft.world.item.CreativeModeTab
import net.wiredtomato.burgered.service.CreativeModeTabBuilder

@ServiceImpl(CreativeModeTabBuilder::class)
class NeoForgeCreativeModeTabBuilder : CreativeModeTabBuilder {
    override fun createBuilder(): CreativeModeTab.Builder {
        return CreativeModeTab.builder()
    }
}