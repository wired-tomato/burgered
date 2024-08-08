package net.wiredtomato.burgered.platform

import dev.architectury.injectables.annotations.ExpectPlatform
import net.minecraft.world.level.ItemLike

object DynamicItemRendererRegistry {
    @JvmStatic
    @ExpectPlatform
    fun register(item: ItemLike, renderer: DynamicItemRenderer) {
        throw AssertionError()
    }

    @JvmStatic
    @ExpectPlatform
    fun getRenderer(item: ItemLike): DynamicItemRenderer? {
        throw AssertionError()
    }
}
