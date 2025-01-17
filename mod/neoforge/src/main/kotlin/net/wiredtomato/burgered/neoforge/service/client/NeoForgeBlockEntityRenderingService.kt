package net.wiredtomato.burgered.neoforge.service.client

import deplatformed.ServiceImpl
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.neoforged.neoforge.client.event.EntityRenderersEvent
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.service.client.BlockEntityRenderingService

@ServiceImpl(BlockEntityRenderingService::class)
class NeoForgeBlockEntityRenderingService : BlockEntityRenderingService {
    override fun <T : BlockEntity> registerRenderer(
        type: KSupplier<BlockEntityType<T>>,
        renderer: BlockEntityRendererProvider<in T>
    ) {
        toRegister.add(Data(type, renderer))
    }

    private class Data<T : BlockEntity>(
        val type: KSupplier<BlockEntityType<T>>,
        val provider: BlockEntityRendererProvider<in T>
    )

    companion object {
        private val toRegister = mutableListOf<Data<*>>()

        fun registerEntityRenderers(event: EntityRenderersEvent.RegisterRenderers) {
            fun Data<*>.register() {
                event.registerBlockEntityRenderer(type(), provider)
            }

            toRegister.forEach { it.register() }
        }
    }
}