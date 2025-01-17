package net.wiredtomato.burgered.service.client

import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.service.Services

interface BlockEntityRenderingService {
    fun <T : BlockEntity> registerRenderer(type: KSupplier<BlockEntityType<T>>, renderer: BlockEntityRendererProvider<in T>)
}

val BlockEntityRenderingServiceImpl = Services.getService<BlockEntityRenderingService>()
