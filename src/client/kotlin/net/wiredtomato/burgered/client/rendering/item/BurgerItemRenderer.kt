package net.wiredtomato.burgered.client.rendering.item

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.render.model.json.ModelTransformationMode
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.item.ItemStack
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.minecraft.util.math.Axis
import net.minecraft.util.random.RandomSeed
import net.wiredtomato.burgered.api.Burger
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.item.components.BurgerComponent
import org.joml.Quaternionf
import org.joml.Vector3f
import kotlin.math.roundToInt
import kotlin.random.Random

object BurgerItemRenderer : DynamicItemRenderer {
    private val sloppinessCache: MutableMap<Triple<Double, Identifier, Int>, RotationOffsets> = mutableMapOf()

    override fun render(
        stack: ItemStack,
        mode: ModelTransformationMode,
        matrices: MatrixStack,
        vertexConsumers: VertexConsumerProvider,
        light: Int,
        overlay: Int
    ) {
        matrices.push()
        val client = MinecraftClient.getInstance()
        val itemRenderer = client.itemRenderer
        val burger = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent())

        itemOffsets(matrices, mode)
        burger.ingredients().forEachIndexed { i, ingredient ->
            val offsets = sloppinessOffset(burger, ingredient, i)
            matrices.push()
            ingredientOffset(itemRenderer, matrices, mode, ingredient)
            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(offsets.x))
            if (mode != ModelTransformationMode.GUI) matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(offsets.y))
            matrices.rotate(Axis.Z_POSITIVE.rotationDegrees(offsets.z))

            itemRenderer.renderItem(
                ItemStack(ingredient),
                mode,
                light,
                overlay,
                matrices,
                vertexConsumers,
                client.world,
                0
            )

            matrices.pop()
            matrices.translate(0.0, ingredient.modelHeight() / 16.0, 0.0)
        }

        matrices.pop()
    }

    fun BurgerIngredient.id(): Identifier = Registries.ITEM.getId(asItem())

    fun sloppinessOffset(burger: Burger, ingredient: BurgerIngredient, index: Int): RotationOffsets {
        val sloppiness = (burger.sloppiness() * 100).roundToInt() / 100.0
        if (sloppiness <= 0) return RotationOffsets(0f, 0f, 0f)

        return sloppinessCache.computeIfAbsent(Triple(sloppiness, ingredient.id(), index)) {
            val random = Random(RandomSeed.generateUniqueSeed())
            val degreesX = random.nextDouble(-10.0 * sloppiness, 10.0 * sloppiness).toFloat()
            val degreesY = random.nextDouble(-90.0 * sloppiness, 90.0 * sloppiness).toFloat()
            val degreesZ = random.nextDouble(-10.0 * sloppiness, 10.0 * sloppiness).toFloat()

            RotationOffsets(degreesX, degreesY, degreesZ)
        }
    }

    fun itemOffsets(matrices: MatrixStack, mode: ModelTransformationMode) {
        matrices.translate(0.5f, 0.5f, 0.5f)
    }

    fun ingredientOffset(itemRenderer: ItemRenderer, matrices: MatrixStack, mode: ModelTransformationMode, ingredient: BurgerIngredient) {
        val model = itemRenderer.models.getModel(ingredient.asItem()) ?: return
        val transform = model.transformation.getTransformation(mode)

        if (!transform.scale.isIdentity()) {
            matrices.scale(1 / transform.scale.x, 1 / transform.scale.y, 1 / transform.scale.z)
        }
        if (!transform.translation.isZeroed()) {
            matrices.translate(-transform.translation.x, -transform.translation.y, -transform.translation.z)
        }
        if (!transform.rotation.isZeroed()) {
            matrices.rotate(
                Quaternionf()
                    .rotationXYZ(
                        -transform.rotation.x.toRadians(),
                        -transform.rotation.y.toRadians(),
                        -transform.rotation.z.toRadians()
                    )
            )

        }

        matrices.translate(0f, if (mode == ModelTransformationMode.GUI) 0f else 8f / 16, 0f)
    }

    fun Vector3f.isIdentity() = this.x == 1f && this.y == 1f && this.z == 1f
    fun Vector3f.isZeroed() = this.x == 0f && this.y == 0f && this.z == 0f

    fun Float.toRadians() = this * Math.PI.toFloat() / 180

    data class RotationOffsets(val x: Float, val y: Float, val z: Float)
}