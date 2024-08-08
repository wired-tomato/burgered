package net.wiredtomato.burgered.platform.fabric

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.world.level.ItemLike
import net.wiredtomato.burgered.fabric.WrappedDynamicItemRenderer
import net.wiredtomato.burgered.platform.DynamicItemRenderer

object DynamicItemRendererRegistryImpl {
    @JvmStatic
    fun register(item: ItemLike, renderer: DynamicItemRenderer) {
        BuiltinItemRendererRegistry.INSTANCE.register(item, WrappedDynamicItemRenderer(renderer))
    }

    @JvmStatic
    fun getRenderer(item: ItemLike): DynamicItemRenderer? {
        return BuiltinItemRendererRegistry.INSTANCE.get(item) as? WrappedDynamicItemRenderer
    }
}
