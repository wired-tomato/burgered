package net.wiredtomato.burgered.api.rendering.client

import com.google.common.collect.BiMap
import com.google.common.collect.HashBiMap
import com.mojang.blaze3d.vertex.PoseStack
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.wiredtomato.burgered.api.burger.Burger
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredientInstance
import net.wiredtomato.burgered.client.rendering.item.DefaultIngredientRenderer
import org.joml.Vector3d

interface BurgerIngredientRenderer {
    fun render(
        burger: Burger,
        ingredient: BurgerIngredientInstance,
        ingredientIndex: Int,
        vanillaDisplayContext: ItemDisplayContext,
        burgeredDisplayContext: ItemDisplayContext,
        itemRenderer: ItemRenderer,
        itemModelResolver: ItemModelResolver,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int,
        hasFoil: Boolean
    ): Vector3d

    companion object {
        private val idMap: BiMap<ResourceLocation, BurgerIngredientRenderer> = HashBiMap.create()

        fun register(id: ResourceLocation, renderer: BurgerIngredientRenderer) {
            val old = idMap.put(id, renderer)
            if (old != null) {
                throw IllegalArgumentException("Ingredient renderer with id: $id already registered!")
            }
        }

        fun defaultRenderer(): BurgerIngredientRenderer {
            return idMap[DefaultIngredientRenderer.ID] ?: throw IllegalStateException("Default ingredient renderer has not been registered!")
        }

        fun getRenderer(id: ResourceLocation): BurgerIngredientRenderer? {
            return idMap[id]
        }
    }
}