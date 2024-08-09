package net.wiredtomato.burgered.recipe

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.RecipeSerializer

class GrillingRecipeSerializer : RecipeSerializer<GrillingRecipe> {
    private val _codec = RecordCodecBuilder.mapCodec { instance ->
        val group = instance.group(
            Codec.STRING.optionalFieldOf("group", "").forGetter(GrillingRecipe::getGroup),
            CookingBookCategory.CODEC.fieldOf("category").orElse(CookingBookCategory.MISC)
                .forGetter(GrillingRecipe::category),
            Ingredient.CODEC_NONEMPTY.fieldOf("ingredient").forGetter(GrillingRecipe::ingredient),
            ItemStack.OPTIONAL_CODEC.fieldOf("transform").forGetter(GrillingRecipe::transform),
            ItemStack.CODEC.fieldOf("result").forGetter(GrillingRecipe::result),
            Codec.FLOAT.fieldOf("experience").orElse(0.0F).forGetter(GrillingRecipe::getExperience),
            Codec.INT.fieldOf("cookingtime").orElse(100).forGetter(GrillingRecipe::getCookingTime)
        )
        group.apply(instance, ::GrillingRecipe)
    }

    private val _streamCodec = StreamCodec.of(::streamEncode, ::streamDecode)

    override fun codec(): MapCodec<GrillingRecipe> = _codec
    override fun streamCodec(): StreamCodec<RegistryFriendlyByteBuf, GrillingRecipe> = _streamCodec

    private fun streamEncode(buf: RegistryFriendlyByteBuf, recipe: GrillingRecipe) {
        buf.writeUtf(recipe.group)
        buf.writeEnum(recipe.category())
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.ingredient)
        ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, recipe.transform)
        ItemStack.STREAM_CODEC.encode(buf, recipe.result)
        buf.writeFloat(recipe.experience)
        buf.writeInt(recipe.cookingTime)
    }

    private fun streamDecode(buf: RegistryFriendlyByteBuf): GrillingRecipe {
        val group = buf.readUtf()
        val category = buf.readEnum(CookingBookCategory::class.java)
        val ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf)
        val transform = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf)
        val result = ItemStack.STREAM_CODEC.decode(buf)
        val experience = buf.readFloat()
        val cookingTime = buf.readInt()

        return GrillingRecipe(group, category, ingredient, transform, result, experience, cookingTime)
    }
}