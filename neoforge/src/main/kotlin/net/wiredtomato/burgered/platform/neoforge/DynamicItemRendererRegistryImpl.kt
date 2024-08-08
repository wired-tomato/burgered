package net.wiredtomato.burgered.platform.neoforge

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.level.ItemLike
import net.wiredtomato.burgered.platform.DynamicItemRenderer

object DynamicItemRendererRegistryImpl {
    private val renderers = mutableMapOf<Item, DynamicItemRenderer>()

    @JvmStatic
    fun register(item: ItemLike, renderer: DynamicItemRenderer) {
        if (renderers.putIfAbsent(item.asItem(), renderer) != null) {
            throw IllegalArgumentException("Item ${BuiltInRegistries.ITEM.getKey(item.asItem())} already has a registered renderer!")
        }
    }

    @JvmStatic
    fun getRenderer(item: ItemLike): DynamicItemRenderer? {
        return renderers[item.asItem()]
    }
}
