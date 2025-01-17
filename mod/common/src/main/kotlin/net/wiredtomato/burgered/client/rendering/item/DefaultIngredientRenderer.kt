package net.wiredtomato.burgered.client.rendering.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.client.renderer.item.ItemModelResolver
import net.minecraft.client.renderer.item.ItemStackRenderState
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.level.levelgen.RandomSupport
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.burger.Burger
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredient
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredientInstance
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings
import net.wiredtomato.burgered.api.rendering.WithModelHeight
import net.wiredtomato.burgered.api.rendering.client.BurgerIngredientRenderer
import net.wiredtomato.burgered.client.config.BurgeredClientConfig
import net.wiredtomato.burgered.client.rendering.item.BurgerItemRenderer.RotationOffsets
import net.wiredtomato.burgered.client.rendering.item.BurgerItemRenderer.id
import org.joml.Vector3d
import org.joml.Vector3f
import kotlin.math.roundToInt
import kotlin.random.Random

object DefaultIngredientRenderer : BurgerIngredientRenderer {
    val ID = Burgered.modLoc("default")

    private val sloppinessCache: MutableMap<Triple<Double, ResourceLocation, Int>, RotationOffsets> = mutableMapOf()
    private var lastMaxRot = Vector3f()

    override fun render(
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
    ): Vector3d {
        matrices.pushPose()
        val ingredientStack = ingredient.stack()
        val renderSettings = ingredient.renderSettings()

        val maxRot = Vector3f(
            BurgeredClientConfig.maxSloppinessRotationX,
            BurgeredClientConfig.maxSloppinessRotationY,
            BurgeredClientConfig.maxSloppinessRotationZ
        )

        if (lastMaxRot != maxRot) {
            sloppinessCache.clear()
        }

        lastMaxRot = maxRot

        val offsets = sloppinessOffset(burger, ingredient.ingredient, ingredientIndex)
        matrices.mulPose(Axis.XP.rotationDegrees(offsets.x))
        matrices.mulPose(Axis.YP.rotationDegrees(offsets.y))
        matrices.mulPose(Axis.ZP.rotationDegrees(offsets.z))

        val stackRenderState = ItemStackRenderState()
        itemModelResolver.updateForTopItem(
            stackRenderState,
            ingredientStack,
            burgeredDisplayContext,
            false,
            null,
            null,
            0
        )

        stackRenderState.render(
            matrices,
            vertexConsumers,
            light,
            overlay,
        )
        matrices.popPose()

        return when (renderSettings) {
            is WithModelHeight -> {
                Vector3d(
                    0.0,
                    if (renderSettings.modelHeight == 0.0) 0.001 else (renderSettings.modelHeight / 16.0) * renderSettings.renderScale.y,
                    0.0
                )
            }
            is IngredientRenderSettings.ItemModel2d -> {
                Vector3d(
                    0.0,
                    (1.0 / 16.0) * renderSettings.renderScale.y,
                    0.0
                )
            }
            else -> Vector3d()
        }
    }

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
}
