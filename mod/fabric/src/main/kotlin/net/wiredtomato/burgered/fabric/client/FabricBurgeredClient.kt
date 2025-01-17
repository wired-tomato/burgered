package net.wiredtomato.burgered.fabric.client

import com.mojang.serialization.MapCodec
import net.fabricmc.api.ClientModInitializer
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.client.renderer.special.SpecialModelRenderers
import net.minecraft.resources.ResourceLocation
import net.wiredtomato.burgered.api.event.client.ClientEvents
import net.wiredtomato.burgered.api.event.client.ClientEvents.REGISTER_SPECIAL_RENDERERS
import net.wiredtomato.burgered.client.BurgeredClient

object FabricBurgeredClient : ClientModInitializer {
    override fun onInitializeClient() {
        BurgeredClient.init()
        REGISTER_SPECIAL_RENDERERS.invoker()
            .registerRenderers(ClientEvents.RegisterSpecialRenderers.Registry { id: ResourceLocation, codec: MapCodec<out SpecialModelRenderer.Unbaked> ->
                SpecialModelRenderers.ID_MAPPER.put(id, codec)
            })
    }
}