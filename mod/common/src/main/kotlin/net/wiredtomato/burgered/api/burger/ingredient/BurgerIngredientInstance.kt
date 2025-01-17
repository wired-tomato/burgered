package net.wiredtomato.burgered.api.burger.ingredient

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.world.item.ItemStack
import net.wiredtomato.burgered.api.ConsumptionEffect
import net.wiredtomato.burgered.api.burger.ingredient.IngredientQuality.Companion.quality
import net.wiredtomato.burgered.api.burger.ingredient.IngredientQuality.Companion.withQuality
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings
import net.wiredtomato.burgered.api.util.withPatch
import net.wiredtomato.burgered.init.BurgeredDataComponents
import com.mojang.datafixers.util.Pair as DataFixerPair

data class BurgerIngredientInstance(
    val ingredient: BurgerIngredient,
    private var patches: DataComponentPatch,
    val quality: IngredientQuality,
) {
    init {
        patches = patches.forget { it == BurgeredDataComponents.QUALITY }
    }

    constructor(ingredient: BurgerIngredient) : this(ingredient, ingredient.asItem().defaultInstance.componentsPatch, ingredient.defaultQuality())
    constructor(ingredient: BurgerIngredient, stack: ItemStack) : this(ingredient, stack.componentsPatch, stack.quality())

    fun patch(): DataComponentPatch = patches
    fun item() = ingredient.asItem()
    fun stack(): ItemStack {
        return ingredient.asItem().defaultInstance.withPatch(patches).withQuality(quality)
    }

    fun hunger(): Int = ingredient.hunger(this)
    fun saturation(): Float = ingredient.saturation(this)
    fun consumptionEffects(): List<ConsumptionEffect> = ingredient.consumptionEffects(this)
    fun renderSettings(): IngredientRenderSettings = ingredient.renderSettings(this)
    fun defaultQuality(): IngredientQuality = ingredient.defaultQuality()

    companion object {
        private val resultTransformer = object : Codec.ResultFunction<BurgerIngredientInstance> {
            override fun <T : Any?> apply(
                ops: DynamicOps<T>,
                input: T,
                result: DataResult<DataFixerPair<BurgerIngredientInstance, T>>
            ): DataResult<DataFixerPair<BurgerIngredientInstance, T>> {
                if (result.isSuccess) return result
                val simpleResult: DataResult<DataFixerPair<BurgerIngredient, T>> = BurgerIngredient.VALUE_CODEC.decode(ops, input)
                val transformedResult: DataResult<DataFixerPair<BurgerIngredientInstance, T>> = simpleResult
                    .map {
                        val defaultStack = it.first.asItem().defaultInstance
                        val quality = defaultStack.quality()
                        DataFixerPair(BurgerIngredientInstance(it.first, defaultStack.componentsPatch, quality), it.second)
                    }

                return transformedResult
            }

            override fun <T : Any?> coApply(
                ops: DynamicOps<T>,
                input: BurgerIngredientInstance,
                result: DataResult<T>
            ): DataResult<T> = result
        }

        val PATCH_CODEC = DataComponentPatch.CODEC
        val CODEC: Codec<BurgerIngredientInstance> = RecordCodecBuilder.create { builder ->
            builder.group(
                BurgerIngredient.VALUE_CODEC.fieldOf("ingredient").forGetter(BurgerIngredientInstance::ingredient),
                PATCH_CODEC.optionalFieldOf("patches", DataComponentPatch.EMPTY)
                    .forGetter(BurgerIngredientInstance::patches),
                IngredientQuality.CODEC.optionalFieldOf("quality", IngredientQuality.NORMAL)
                    .forGetter(BurgerIngredientInstance::quality),
            ).apply(builder, ::BurgerIngredientInstance)
        }.mapResult(resultTransformer)

        fun BurgerIngredient.defaultInstance() = BurgerIngredientInstance(this)
        fun BurgerIngredient.instanceFromStack(stack: ItemStack) = BurgerIngredientInstance(this, stack)
    }
}
