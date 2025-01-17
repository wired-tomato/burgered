package net.wiredtomato.burgered.api.burger.ingredient

import com.mojang.datafixers.util.Pair
import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.Holder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.resources.RegistryFileCodec
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.ItemLike
import net.wiredtomato.burgered.api.ConsumptionEffect
import net.wiredtomato.burgered.api.burger.Burger
import net.wiredtomato.burgered.api.registry.BurgeredRegistries
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings
import net.wiredtomato.burgered.api.util.cast

interface BurgerIngredient : ItemLike {
    fun canBePutOn(ingredientStack: ItemStack, burger: Burger, burgerStack: ItemStack): Boolean
    fun hunger(instance: BurgerIngredientInstance): Int
    fun saturation(instance: BurgerIngredientInstance): Float
    fun consumptionEffects(instance: BurgerIngredientInstance): List<ConsumptionEffect>
    fun renderSettings(instance: BurgerIngredientInstance): IngredientRenderSettings
    fun defaultQuality(): IngredientQuality = IngredientQuality.NORMAL

    class ItemDetached(
        val hunger: Int,
        val saturation: Float,
        val statusEffects: List<ConsumptionEffect>,
        val renderSettings: IngredientRenderSettings,
        val item: Item,
    ) : BurgerIngredient {
        override fun canBePutOn(ingredientStack: ItemStack, burger: Burger, burgerStack: ItemStack): Boolean = true
        override fun hunger(instance: BurgerIngredientInstance): Int = hunger
        override fun saturation(instance: BurgerIngredientInstance): Float = saturation
        override fun consumptionEffects(instance: BurgerIngredientInstance): List<ConsumptionEffect> = statusEffects
        override fun renderSettings(instance: BurgerIngredientInstance): IngredientRenderSettings = renderSettings
        override fun asItem(): Item = item
    }

    companion object {
        val DETACHED_CODEC: Codec<ItemDetached> = RecordCodecBuilder.create { builder ->
            builder.group(
                Codec.INT.fieldOf("hunger").orElse(0).forGetter(ItemDetached::hunger),
                Codec.FLOAT.fieldOf("saturation").orElse(0f).forGetter(ItemDetached::saturation),
                ConsumptionEffect.CODEC.listOf().fieldOf("consumptionEffects").orElse(listOf())
                    .forGetter(ItemDetached::statusEffects),
                IngredientRenderSettings.CODEC.fieldOf("renderSettings").forGetter(ItemDetached::renderSettings),
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(ItemDetached::item),
            ).apply(builder, ::ItemDetached)
        }

        val REGISTRY_CODEC: Codec<Holder<ItemDetached>> =
            RegistryFileCodec.create(BurgeredRegistries.Keys.INGREDIENT, DETACHED_CODEC)

        val CODEC = object : Codec<Holder<BurgerIngredient>> {
            override fun <T : Any?> encode(input: Holder<BurgerIngredient>, ops: DynamicOps<T>, prefix: T): DataResult<T> {
                val ingredient = input.value()
                val inputItem = ingredient.asItem()
                val result = if (ingredient is ItemDetached) {
                    REGISTRY_CODEC.encode(input.cast(), ops, prefix)
                } else if (inputItem is BurgerIngredient) {
                    BuiltInRegistries.ITEM.byNameCodec().encode(inputItem, ops, prefix)
                } else throw IllegalArgumentException("Unsupported input type: ${ingredient::class.qualifiedName}")

                return result
            }

            override fun <T : Any?> decode(ops: DynamicOps<T>, input: T): DataResult<Pair<Holder<BurgerIngredient>, T>> {
                val ingredient: Holder<BurgerIngredient> =
                    REGISTRY_CODEC.decode(ops, input).mapOrElse({ it.first.cast() }) {
                        Holder.direct(BuiltInRegistries.ITEM.byNameCodec().decode(ops, input).orThrow.first as BurgerIngredient)
                    }

                return DataResult.success(Pair.of(ingredient, input))
            }
        }

        val VALUE_CODEC = CODEC.xmap({ it.value() }, { Holder.direct(it) })
    }
}
