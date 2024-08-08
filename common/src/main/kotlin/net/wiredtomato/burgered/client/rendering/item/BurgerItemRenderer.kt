package net.wiredtomato.burgered.client.rendering.item

import com.mojang.blaze3d.vertex.PoseStack
import com.mojang.math.Axis
import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.MultiBufferSource
import net.minecraft.client.renderer.entity.ItemRenderer
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemDisplayContext
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.levelgen.RandomSupport
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.client.config.BurgeredClientConfig
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.VanillaItemBurgerIngredientItem
import net.wiredtomato.burgered.item.components.BurgerComponent
import net.wiredtomato.burgered.platform.DynamicItemRenderer
import org.joml.Quaternionf
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
        val newMode = if (BurgeredClientConfig.renderNoTransform) ItemDisplayContext.NONE else ctx

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

            val offsets = sloppinessOffset(burger, ingredient.second, i)
            matrices.pushPose()
            ingredientOffset(itemRenderer, matrices, ctx, newMode, ingredient.first, ingredient.second)
            matrices.mulPose(Axis.XP.rotationDegrees(offsets.x))
            if (newMode != ItemDisplayContext.GUI) matrices.mulPose(Axis.YP.rotationDegrees(offsets.y))
            matrices.mulPose(Axis.ZP.rotationDegrees(offsets.z))

            itemRenderer.renderStatic(
                ingredient.first,
                newMode,
                light,
                overlay,
                matrices,
                vertexConsumers,
                client.level,
                0
            )

            matrices.popPose()
            matrices.translate(
                0.0,
                (ingredient.second.modelHeight(ingredient.first) / 16.0) * getIngredientScale(ingredient.first, ingredient.second),
                0.0
            )
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

    fun ingredientOffset(
        itemRenderer: ItemRenderer,
        matrices: PoseStack,
        originalMode: ItemDisplayContext,
        mode: ItemDisplayContext,
        ingredientStack: ItemStack,
        ingredient: BurgerIngredient
    ) {
        val model = if (ingredient is VanillaItemBurgerIngredientItem) {
            itemRenderer.itemModelShaper.getItemModel(ingredient.getVanillaStack(ingredientStack)) ?: return
        } else itemRenderer.itemModelShaper.getItemModel(ingredient.asItem()) ?: return

        val transform = model.transforms.getTransform(mode)

        val scale = transform.scale
        if (!transform.scale.isIdentity()) {
            matrices.scale(1 / transform.scale.x, 1 / transform.scale.y, 1 / transform.scale.z)
        }

        if (!transform.translation.isZeroed()) {
            matrices.translate(-transform.translation.x, -transform.translation.y, -transform.translation.z)
        }

        if (!transform.rotation.isZeroed()) {
            matrices.mulPose(
                Quaternionf()
                    .rotationXYZ(
                        -transform.rotation.x.toRadians(),
                        -transform.rotation.y.toRadians(),
                        -transform.rotation.z.toRadians()
                    )
            )
        }

        if (ingredient is BlockItem || (ingredient is VanillaItemBurgerIngredientItem && ingredient.shouldApplyBlockTransformations(
                ingredientStack
            ))
        ) {
            matrices.scale(0.5f, 0.5f, 0.5f)
            matrices.translate(
                0.0,
                if (originalMode == ItemDisplayContext.GUI) -0.5 / scale.y else 0.5 / scale.y,
                0.0
            )
        }

        if (ingredient is VanillaItemBurgerIngredientItem) run vanillaCondition@{
            val vanillaStack = ingredient.getVanillaStack(ingredientStack)
            if (vanillaStack.item is BlockItem && ingredient.shouldApplyBlockTransformations(ingredientStack)) return@vanillaCondition

            matrices.scale(0.5f, 0.5f, 0.5f)
            matrices.translate(-0.03, 0.035, 0.03)

            if (originalMode == ItemDisplayContext.GUI && mode != ItemDisplayContext.GUI) {
                matrices.translate(0.0, -1.0, 0.0)
            }

            when (mode) {
                ItemDisplayContext.GROUND -> {
                    matrices.translate(0.02, 0.23, -0.15)
                }

                ItemDisplayContext.GUI -> {
                    matrices.translate(0.0, -1.0, 0.0)
                }

                ItemDisplayContext.FIRST_PERSON_RIGHT_HAND, ItemDisplayContext.FIRST_PERSON_LEFT_HAND -> {
                    matrices.translate(-0.15, 0.42, -0.02)
                    matrices.mulPose(Axis.YP.rotationDegrees(0f))
                    matrices.mulPose(Axis.ZN.rotationDegrees(65f))
                    matrices.mulPose(Axis.XP.rotationDegrees(25f))
                }

                ItemDisplayContext.THIRD_PERSON_RIGHT_HAND, ItemDisplayContext.THIRD_PERSON_LEFT_HAND -> {
                    matrices.translate(0.0, 0.42, -0.1)
                }

                else -> {}
            }

            matrices.mulPose(Axis.XP.rotationDegrees(90f))
        } else {
            matrices.translate(
                0f,
                if (originalMode == ItemDisplayContext.GUI) 0f
                else scale.y * ((8f / 16) + if (ingredient.modelHeight(ingredientStack) == 0.0) 0.001f else 0f),
                0f
            )
        }
    }

    fun getIngredientScale(ingredientStack: ItemStack, ingredient: BurgerIngredient): Float {
        return if ((ingredient is VanillaItemBurgerIngredientItem && ingredient.shouldApplyBlockTransformations(ingredientStack)) || ingredient is BlockItem) 0.5f else 1f
    }

    fun Vector3f.isIdentity() = this.x == 1f && this.y == 1f && this.z == 1f
    fun Vector3f.isZeroed() = this.x == 0f && this.y == 0f && this.z == 0f

    fun Float.toRadians() = this * Math.PI.toFloat() / 180

    data class RotationOffsets(val x: Float, val y: Float, val z: Float)
}
