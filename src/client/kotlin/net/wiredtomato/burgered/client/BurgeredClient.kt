package net.wiredtomato.burgered.client

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories
import net.wiredtomato.burgered.client.rendering.block.BurgerStackerEntityRenderer
import net.wiredtomato.burgered.client.rendering.item.BurgerItemRenderer
import net.wiredtomato.burgered.client.rendering.item.VanillaIngredientRenderer
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.init.BurgeredItems

object BurgeredClient {
    fun clientInit() {
        BuiltinItemRendererRegistry.INSTANCE.register(BurgeredItems.BURGER, BurgerItemRenderer)
        BuiltinItemRendererRegistry.INSTANCE.register(BurgeredItems.VANILLA_INGREDIENT, VanillaIngredientRenderer)
        BlockEntityRendererFactories.register(BurgeredBlockEntities.BURGER_STACKER, ::BurgerStackerEntityRenderer)
    }
}