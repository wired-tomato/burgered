package net.wiredtomato.burgered.fabric.service.client

import deplatformed.ServiceImpl
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.service.client.BlockEntityRenderingService

@ServiceImpl(BlockEntityRenderingService::class)
class FabricBlockEntityRenderingService : BlockEntityRenderingService {
    override fun <T : BlockEntity> registerRenderer(
        type: KSupplier<BlockEntityType<T>>,
        renderer: BlockEntityRendererProvider<in T>
    ) {
        BlockEntityRenderers.register(type(), renderer)
    }
}