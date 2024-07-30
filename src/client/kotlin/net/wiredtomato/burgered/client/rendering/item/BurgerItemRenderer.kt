package net.wiredtomato.burgered.client.rendering.item

import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry.DynamicItemRenderer
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.VertexConsumerProvider
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
            matrices.rotate(Axis.X_POSITIVE.rotationDegrees(offsets.x))
            matrices.rotate(Axis.Y_POSITIVE.rotationDegrees(offsets.y))
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
            val degreesX = random.nextDouble(-90.0 * sloppiness, 90.0 * sloppiness).toFloat()
            val degreesY = random.nextDouble(-20.0 * sloppiness, 20.0 * sloppiness).toFloat()
            val degreesZ = random.nextDouble(-90.0 * sloppiness, 90.0 * sloppiness).toFloat()

            RotationOffsets(degreesX, degreesY, degreesZ)
        }
    }

    fun itemOffsets(matrices: MatrixStack, mode: ModelTransformationMode) {
        matrices.translate(0.5f, if (mode == ModelTransformationMode.GUI) 0f else 0.51f, 0.5f)
    }

    data class RotationOffsets(val x: Float, val y: Float, val z: Float)
}