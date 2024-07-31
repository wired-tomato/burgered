package net.wiredtomato.burgered.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.recipe.RecipeCategory
import net.minecraft.recipe.RecipeSerializer
import net.wiredtomato.burgered.util.packetCodec

class SpecialSmithingRecipeSerializer<T : CategorizedSmithingRecipe>(
    factory: Factory<T>
) : RecipeSerializer<T> {
    private val _codec: MapCodec<T> = RecordCodecBuilder.mapCodec { builder ->
        builder.group(
            Codec.INT.xmap(::fromOrdinal, ::toOrdinal).fieldOf("category").forGetter(CategorizedSmithingRecipe::category)
        ).apply(builder, factory::create)
    }

    private val _packetCodec: PacketCodec<RegistryByteBuf, T> = _codec.packetCodec()

    override fun getCodec(): MapCodec<T> = _codec

    override fun getPacketCodec(): PacketCodec<RegistryByteBuf, T> = _packetCodec

    fun interface Factory<T : CategorizedSmithingRecipe> {
        fun create(category: RecipeCategory): T
    }

    fun toOrdinal(category: RecipeCategory): Int = category.ordinal
    fun fromOrdinal(ordinal: Int): RecipeCategory = RecipeCategory.entries[ordinal]
}