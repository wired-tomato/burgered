package net.wiredtomato.burgered.client.rendering.block

import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity

class BurgerStackerEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<BurgerStackerEntity> {
    private val itemRenderer = ctx.itemRenderer

    override fun render(
        entity: BurgerStackerEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.push()

        matrices.translate(0.5, 0.125, 0.5)
        val burger = entity.renderStack()
        itemRenderer.renderItem(
            burger,
            ModelTransformationMode.NONE,
            light,
            overlay,
            matrices,
            vertexConsumers,
            entity.world,
            0
        )

        matrices.pop()
    }
}