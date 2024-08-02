package net.wiredtomato.burgered.client.rendering.item

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.wiredtomato.burgered.item.VanillaItemBurgerIngredientItem

object VanillaIngredientRenderer : DynamicItemRenderer {
    override fun render(
        stack: ItemStack,
        mode: ModelTransformationMode,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.push()
        val item = stack.item
        if (item !is VanillaItemBurgerIngredientItem) return
        val renderStack = item.getVanillaStack(stack)

        val client = MinecraftClient.getInstance()
        val itemRenderer = client.itemRenderer
        BurgerItemRenderer.itemOffsets(matrices, mode)
        itemRenderer.renderItem(
            renderStack,
            mode,
            light,
            overlay,
            matrices,
            vertexConsumers,
            client.world,
            0
        )

        matrices.pop()
    }
}