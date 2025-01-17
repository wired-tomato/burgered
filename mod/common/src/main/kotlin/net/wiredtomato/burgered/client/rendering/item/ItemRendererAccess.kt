package net.wiredtomato.burgered.client.rendering.item

import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.item.ItemModelResolver

interface ItemRendererAccess {
    fun `burgered$getModelResolver`(): ItemModelResolver
}

fun ItemRenderer.getModelResolver(): ItemModelResolver {
    return (this as ItemRendererAccess).`burgered$getModelResolver`()
}
