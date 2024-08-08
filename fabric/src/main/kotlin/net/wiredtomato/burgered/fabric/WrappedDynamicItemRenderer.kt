package net.wiredtomato.burgered.fabric

import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.wiredtomato.burgered.platform.DynamicItemRenderer
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer as FabricDynamicItemRenderer

class WrappedDynamicItemRenderer(private val renderer: DynamicItemRenderer) : FabricDynamicItemRenderer, DynamicItemRenderer {
    override fun render(
        p0: ItemStack,
        p1: ItemDisplayContext,
        p2: PoseStack,
        p3: MultiBufferSource,
        p4: Int,
        p5: Int
    ) {
        renderer.render(p0, p1, p2, p3, p4, p5)
    }
}
