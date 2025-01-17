package net.wiredtomato.burgered.client.rendering.block

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.world.item.ItemDisplayContext
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity

class BurgerStackerEntityRenderer(ctx: BlockEntityRendererProvider.Context) : BlockEntityRenderer<BurgerStackerEntity> {
    private val itemRenderer = ctx.itemRenderer

    override fun render(
        entity: BurgerStackerEntity,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {
        matrices.pushPose()

        matrices.translate(0.5, 0.125, 0.5)
        val burger = entity.renderStack()
        itemRenderer.renderStatic(
            burger,
            ItemDisplayContext.NONE,
            light,
            overlay,
            matrices,
            vertexConsumers,
            entity.level,
            0
        )

        matrices.popPose()
    }
}