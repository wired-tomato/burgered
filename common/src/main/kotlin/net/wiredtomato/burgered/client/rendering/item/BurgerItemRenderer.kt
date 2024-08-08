package net.wiredtomato.burgered.client.rendering.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.resources.model.BakedModel
import net.minecraft.client.resources.model.ModelManager
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.levelgen.RandomSupport
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings
import net.wiredtomato.burgered.api.rendering.ModelId
import net.wiredtomato.burgered.api.rendering.WithCustomModel
import net.wiredtomato.burgered.api.rendering.WithModelHeight
import net.wiredtomato.burgered.client.config.BurgeredClientConfig
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.BurgerComponent
import net.wiredtomato.burgered.platform.DynamicItemRenderer
import org.joml.Vector3f
import kotlin.math.roundToInt
import kotlin.random.Random

object BurgerItemRenderer : DynamicItemRenderer {
    private val sloppinessCache: MutableMap<Triple<Double, ResourceLocation, Int>, RotationOffsets> = mutableMapOf()
    private var lastMaxRot = Vector3f()

    override fun render(
        stack: ItemStack,
        ctx: ItemDisplayContext,
        matrices: PoseStack,
        vertexConsumers: MultiBufferSource,
        light: Int,
        overlay: Int
    ) {
        val newMode = ItemDisplayContext.NONE

        val maxRot = Vector3f(
            BurgeredClientConfig.maxSloppinessRotationX,
            BurgeredClientConfig.maxSloppinessRotationY,
            BurgeredClientConfig.maxSloppinessRotationZ
        )

        if (lastMaxRot != maxRot) {
            sloppinessCache.clear()
        }

        lastMaxRot = maxRot

        matrices.pushPose()
        val client = Minecraft.getInstance()
        val itemRenderer = client.itemRenderer
        val burger = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent())

        itemOffsets(matrices, newMode)
        burger.ingredients().forEachIndexed { i, ingredient ->
            if (i >= BurgeredClientConfig.maxRenderedBurgerIngredients) return@forEachIndexed
            val renderSettings = ingredient.second.renderSettings(ingredient.first)
            val model = getModel(itemRenderer, client.modelManager, ingredient.first, ingredient.second)

            val offsets = sloppinessOffset(burger, ingredient.second, i)
            matrices.pushPose()
            ingredientOffset(matrices, ctx, ingredient.first, ingredient.second)
            matrices.mulPose(Axis.XP.rotationDegrees(offsets.x))
            matrices.mulPose(Axis.YP.rotationDegrees(offsets.y))
            matrices.mulPose(Axis.ZP.rotationDegrees(offsets.z))

            itemRenderer.render(
                ingredient.first,
                newMode,
                false,
                matrices,
                vertexConsumers,
                light,
                overlay,
                model
            )

            matrices.popPose()
            when (renderSettings) {
                is WithModelHeight -> {
                    matrices.translate(
                        0.0,
                        (renderSettings.modelHeight / 16.0) * renderSettings.renderScale.y,
                        0.0
                    )
                }
                is IngredientRenderSettings.ItemModel2d -> {
                    matrices.translate(
                        0.0,
                        (1.0 / 16.0) * renderSettings.renderScale.y,
                        0.0
                    )
                }
                else -> {}
            }
        }

        matrices.popPose()
    }

    fun BurgerIngredient.id(): ResourceLocation = BuiltInRegistries.ITEM.getKey(asItem())

    fun sloppinessOffset(burger: Burger, ingredient: BurgerIngredient, index: Int): RotationOffsets {
        val sloppiness = (burger.sloppiness() * 100).roundToInt() / 100.0
        if (sloppiness <= 0) return RotationOffsets(0f, 0f, 0f)

        return sloppinessCache.computeIfAbsent(Triple(sloppiness, ingredient.id(), index)) {
            val random = Random(RandomSupport.generateUniqueSeed())
            val rotX = BurgeredClientConfig.maxSloppinessRotationX
            val rotY = BurgeredClientConfig.maxSloppinessRotationY
            val rotZ = BurgeredClientConfig.maxSloppinessRotationZ
            val degreesX = random.nextDouble(-rotX * sloppiness, rotX * sloppiness).toFloat()
            val degreesY = random.nextDouble(-rotY * sloppiness, rotY * sloppiness).toFloat()
            val degreesZ = random.nextDouble(-rotZ * sloppiness, rotZ * sloppiness).toFloat()

            RotationOffsets(degreesX, degreesY, degreesZ)
        }
    }

    fun itemOffsets(matrices: PoseStack, mode: ItemDisplayContext) {
        matrices.translate(0.5f, 0.5f, 0.5f)
    }

    fun ModelId.toModelResourceLocation() = ModelResourceLocation(this.model, this.variant)

    fun getModel(
        itemRenderer: ItemRenderer,
        modelManager: ModelManager,
        ingredientStack: ItemStack,
        ingredient: BurgerIngredient
    ): BakedModel {
        val renderSettings = ingredient.renderSettings(ingredientStack)
        return when (renderSettings) {
            is WithCustomModel -> {
                modelManager.getModel(renderSettings.customModelId.toModelResourceLocation())
            }
            else -> {
                itemRenderer.itemModelShaper.getItemModel(ingredientStack)
            }
        }
    }

    fun ingredientOffset(
        matrices: PoseStack,
        originalMode: ItemDisplayContext,
        ingredientStack: ItemStack,
        ingredient: BurgerIngredient
    ) {
        val renderSettings = ingredient.renderSettings(ingredientStack)
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
                    -0.06 / scale.x, 0.07 / scale.y, 0.03 / scale.z
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
        }
    }

    data class RotationOffsets(val x: Float, val y: Float, val z: Float)
}
