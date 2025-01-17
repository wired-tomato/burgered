package net.wiredtomato.burgered.service

import net.minecraft.world.item.CreativeModeTab

interface CreativeModeTabBuilder {
    fun createBuilder(): CreativeModeTab.Builder
}

val CreativeModeTabBuilderImpl = Services.getService<CreativeModeTabBuilder>()
