package net.wiredtomato.burgered.client.rendering.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import com.mojang.serialization.MapCodec
import net.minecraft.client.Minecraft
import net.minecraft.client.model.geom.EntityModelSet
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.special.SpecialModelRenderer
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredient
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredientInstance
import net.wiredtomato.burgered.api.data.component.BurgerComponent
import net.wiredtomato.burgered.api.rendering.*
import net.wiredtomato.burgered.api.rendering.client.BurgerIngredientRenderer
import net.wiredtomato.burgered.client.config.BurgeredClientConfig
import net.wiredtomato.burgered.init.BurgeredDataComponents

object BurgerItemRenderer : SpecialModelRenderer<BurgerComponent> {

    override fun render(
        burger: BurgerComponent?,
        displayContext: ItemDisplayContext,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int,
        hasFoil: Boolean
    ) {
        if (burger == null)
            throw IllegalStateException("Cannot render burger without a BurgerComponent")

        val newMode = ItemDisplayContext.NONE

        matrices.pushPose()
        val client = Minecraft.getInstance()
        val itemRenderer = client.itemRenderer
        val itemModelResolver = itemRenderer.getModelResolver()

        itemOffsets(matrices, newMode)
        burger.ingredients().forEachIndexed { i, ingredientInstance ->
            if (i >= BurgeredClientConfig.maxRenderedBurgerIngredients) return@forEachIndexed

            matrices.pushPose()
            ingredientOffset(matrices, displayContext, ingredientInstance)

            val renderer = getRenderer(ingredientInstance)
            val offset = renderer.render(
                burger,
                ingredientInstance,
                i,
                displayContext,
                newMode,
                itemRenderer,
                itemModelResolver,
                matrices,
                vertexConsumers,
                light,
                overlay,
                hasFoil
            )

            matrices.popPose()
            matrices.translate(offset.x, offset.y, offset.z)
        }

        matrices.popPose()
    }

    fun BurgerIngredient.id(): ResourceLocation = BuiltInRegistries.ITEM.getKey(asItem())

    fun itemOffsets(matrices: PoseStack, mode: ItemDisplayContext) {
        matrices.translate(0.5f, 0.5f, 0.5f)
    }

    fun ModelId.toModelResourceLocation() = ModelResourceLocation(this.model, this.variant)

    fun getRenderer(
        ingredientInstance: BurgerIngredientInstance
    ): BurgerIngredientRenderer {
        val renderSettings = ingredientInstance.renderSettings()
        return when (renderSettings) {
            is WithCustomRenderer -> {
                BurgerIngredientRenderer.getRenderer(renderSettings.rendererId) ?: throw IllegalStateException("Could not find ingredient renderer with id ${renderSettings.rendererId}")
            }
            else -> {
                BurgerIngredientRenderer.defaultRenderer()
            }
        }
    }

    fun ingredientOffset(
        matrices: PoseStack,
        originalMode: ItemDisplayContext,
        ingredientInstance: BurgerIngredientInstance
    ) {
        val renderSettings = ingredientInstance.renderSettings()
        val scale = renderSettings.renderScale
        val offset = renderSettings.offset

        matrices.scale(scale.x.toFloat(), scale.y.toFloat(), scale.z.toFloat())
        matrices.translate(offset.x, offset.y, offset.z)

        when (renderSettings) {
            is IngredientRenderSettings.ItemModel3d -> {
                matrices.translate(
                    0.0,
                    if (originalMode == ItemDisplayContext.GUI) 0.0
                    else scale.y * ((8f / 16) + if (renderSettings.modelHeight == 0.0) 0.001 else 0.0),
                    0.0
                )
            }
            is IngredientRenderSettings.ItemModel2d -> {
                matrices.translate(
                    -0.015 / scale.x,
                    if (originalMode == ItemDisplayContext.GUI) 1.525 / scale.y else 0.0175 / scale.y,
                    0.015 / scale.z
                )

                if (originalMode == ItemDisplayContext.GUI) {
                    matrices.translate(0.0, -2.0 / scale.x, 0.0)
                }

                matrices.mulPose(Axis.XP.rotationDegrees(90f))
            }
            is IngredientRenderSettings.Block -> {
                matrices.translate(
                    0.0,
                    if (originalMode == ItemDisplayContext.GUI) -0.25 / scale.y else 0.25 / scale.y,
                    0.0
                )
            }
            is IngredientRenderSettings.Rendered -> {}
        }
    }

    override fun extractArgument(p0: ItemStack): BurgerComponent? {
        return p0.get(BurgeredDataComponents.BURGER)
    }

    data class RotationOffsets(val x: Float, val y: Float, val z: Float)

    object Unbaked : SpecialModelRenderer.Unbaked {
        val CODEC = MapCodec.unit(Unbaked)

        override fun bake(p0: EntityModelSet): SpecialModelRenderer<*>? {
            return BurgerItemRenderer
        }

        override fun type(): MapCodec<out SpecialModelRenderer.Unbaked?> {
            return CODEC
        }
    }
}
