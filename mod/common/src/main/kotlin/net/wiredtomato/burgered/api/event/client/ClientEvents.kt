package net.wiredtomato.burgered.api.event.client

import com.mojang.serialization.MapCodec
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.resources.ResourceLocation
import net.wiredtomato.burgered.api.event.EventFactory

object ClientEvents {
    val REGISTER_SPECIAL_RENDERERS = EventFactory.loop<RegisterSpecialRenderers>()

    fun interface RegisterSpecialRenderers {
        class Registry(
            val registerFunc: (ResourceLocation, MapCodec<out SpecialModelRenderer.Unbaked>) -> Unit
        ) {
            fun registerRenderer(id: ResourceLocation, codec: MapCodec<out SpecialModelRenderer.Unbaked>) {
                registerFunc(id, codec)
            }
        }

        fun registerRenderers(registry: Registry)
    }
}