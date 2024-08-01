package net.wiredtomato.burgered.client.rendering.block

import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.block.entity.BlockEntityRenderer
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.util.math.Axis
import net.minecraft.util.math.Direction
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.block.entity.GrillEntity
import net.wiredtomato.burgered.client.rendering.item.BurgerItemRenderer

class GrillEntityRenderer(ctx: BlockEntityRendererFactory.Context) : BlockEntityRenderer<GrillEntity> {
    private val itemRender = ctx.itemRenderer

    override fun render(
        grill: GrillEntity,
        tickDelta: Float,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.push()

        grill.renderStacks().forEachIndexed { i, stack ->
            matrices.push()
            val isIngredient = stack.item is BurgerIngredient

            val itemScale = if (isIngredient) {
                BurgerItemRenderer.itemOffsets(matrices, ModelTransformationMode.NONE)
                1f
            } else {
                matrices.scale(0.4f, 0.4f, 0.4f)
                0.4f
            }

            matrices.translate(0.0, 0.75 / itemScale, 0.0)

            val direction = grill.cachedState.get(HorizontalFacingBlock.FACING)

            if (!isIngredient) {
                matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(180f))
                matrices.rotate(Axis.X_POSITIVE.rotationDegrees(270f))
                when (direction) {
                    Direction.NORTH -> {
                        matrices.translate(-0.5 / itemScale, 0.5 / itemScale, 0.0)
                    }
                    Direction.SOUTH -> {
                        matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(180f))
                        matrices.translate(0.5 / itemScale, -0.5 / itemScale, 0.0)
                    }
                    Direction.WEST -> {
                        matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(90f))
                        matrices.translate(0.25 / itemScale, 0.5 / itemScale, 0.0)
                    }
                    Direction.EAST -> {
                        matrices.rotate(Axis.Z_NEGATIVE.rotationDegrees(90f))
                        matrices.translate(-0.75 / itemScale, -0.5 / itemScale, 0.0)
                    }

                    else -> {}
                }
            }

            when (direction) {
                Direction.NORTH, Direction.SOUTH -> {
                    matrices.translate(0.25 / itemScale, 0.0, 0.0)
                    matrices.translate(-0.5 * i / itemScale, 0.0, 0.0)
                } else -> {
                    if (isIngredient) {
                        matrices.translate(0.0, 0.0, -0.25 / itemScale)
                        matrices.translate(0.0, 0.0, 0.5 * i / itemScale)
                    } else {
                        matrices.translate(0.5 * i / itemScale, 0.0, 0.0)
                    }
                }
            }

            itemRender.renderItem(
                stack,
                ModelTransformationMode.NONE,
                light,
                overlay,
                matrices,
                vertexConsumers,
                grill.world,
                0
            )

            matrices.pop()
        }

        matrices.pop()
    }
}