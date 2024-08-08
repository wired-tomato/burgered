package net.wiredtomato.burgered.client.rendering.block

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider
import net.minecraft.core.Direction
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.block.entity.GrillEntity
import net.wiredtomato.burgered.client.rendering.item.BurgerItemRenderer

class GrillEntityRenderer(ctx: BlockEntityRendererProvider.Context) : BlockEntityRenderer<GrillEntity> {
    private val itemRender = ctx.itemRenderer

    override fun render(
        grill: GrillEntity,
        tickDelta: Float,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {
        matrices.pushPose()

        grill.renderStacks().forEachIndexed { i, stack ->
            matrices.pushPose()
            val isIngredient = stack.item is BurgerIngredient

            val itemScale = if (isIngredient) {
                BurgerItemRenderer.itemOffsets(matrices, ItemDisplayContext.NONE)
                1f
            } else {
                matrices.scale(0.4f, 0.4f, 0.4f)
                0.4f
            }

            matrices.translate(0.0, 0.75 / itemScale, 0.0)

            val direction = grill.blockState.getValue(HorizontalDirectionalBlock.FACING)

            if (!isIngredient) {
                matrices.mulPose(Axis.YP.rotationDegrees(180f))
                matrices.mulPose(Axis.XP.rotationDegrees(270f))
                when (direction) {
                    Direction.NORTH -> {
                        matrices.translate(-0.5 / itemScale, 0.5 / itemScale, 0.0)
                    }
                    Direction.SOUTH -> {
                        matrices.mulPose(Axis.ZP.rotationDegrees(180f))
                        matrices.translate(0.5 / itemScale, -0.5 / itemScale, 0.0)
                    }
                    Direction.WEST -> {
                        matrices.mulPose(Axis.ZP.rotationDegrees(90f))
                        matrices.translate(0.25 / itemScale, 0.5 / itemScale, 0.0)
                    }
                    Direction.EAST -> {
                        matrices.mulPose(Axis.ZN.rotationDegrees(90f))
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

            itemRender.renderStatic(
                stack,
                ItemDisplayContext.NONE,
                light,
                overlay,
                matrices,
                vertexConsumers,
                grill.level,
                0
            )

            matrices.popPose()
        }

        matrices.popPose()
    }
}