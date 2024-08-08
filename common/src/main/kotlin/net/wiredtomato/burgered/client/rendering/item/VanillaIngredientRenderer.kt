package net.wiredtomato.burgered.client.rendering.item

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.wiredtomato.burgered.item.VanillaItemBurgerIngredientItem
import net.wiredtomato.burgered.platform.DynamicItemRenderer

object VanillaIngredientRenderer : DynamicItemRenderer {
    override fun render(
        stack: ItemStack,
        ctx: ItemDisplayContext,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {
        matrices.pushPose()
        val item = stack.item
        if (item !is VanillaItemBurgerIngredientItem) return
        val renderStack = item.getVanillaStack(stack)

        val client = Minecraft.getInstance()
        val itemRenderer = client.itemRenderer
        BurgerItemRenderer.itemOffsets(matrices, ctx)
        itemRenderer.renderStatic(
            renderStack,
            ctx,
            light,
            overlay,
            matrices,
            vertexConsumers,
            client.level,
            0
        )

        matrices.popPose()
    }
}