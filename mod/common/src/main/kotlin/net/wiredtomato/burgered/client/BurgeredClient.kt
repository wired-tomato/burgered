package net.wiredtomato.burgered.client

import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.event.client.ClientEvents
import net.wiredtomato.burgered.api.rendering.client.BurgerIngredientRenderer
import net.wiredtomato.burgered.client.config.BurgeredClientConfig
import net.wiredtomato.burgered.client.rendering.block.BurgerStackerEntityRenderer
import net.wiredtomato.burgered.client.rendering.block.GrillEntityRenderer
import net.wiredtomato.burgered.client.rendering.item.BurgerItemRenderer
import net.wiredtomato.burgered.client.rendering.item.DefaultIngredientRenderer
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.service.client.BlockEntityRenderingServiceImpl

object BurgeredClient {
    fun init() {
        BurgeredClientConfig.CONFIG.load()

        ClientEvents.REGISTER_SPECIAL_RENDERERS.register { registry ->
            Burgered.LOGGER.info("Registering burger_renderer")
            registry.registerRenderer(
                Burgered.modLoc("burger_renderer"),
                BurgerItemRenderer.Unbaked.CODEC
            )
        }

        BurgerIngredientRenderer.register(DefaultIngredientRenderer.ID, DefaultIngredientRenderer)
        BlockEntityRenderingServiceImpl.registerRenderer(BurgeredBlockEntities::BURGER_STACKER, ::BurgerStackerEntityRenderer)
        BlockEntityRenderingServiceImpl.registerRenderer(BurgeredBlockEntities::GRILL, ::GrillEntityRenderer)
    }
}