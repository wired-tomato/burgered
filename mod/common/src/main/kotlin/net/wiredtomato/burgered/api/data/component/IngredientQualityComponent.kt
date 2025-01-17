package net.wiredtomato.burgered.api.data.component

import com.mojang.serialization.codecs.RecordCodecBuilder
import net.wiredtomato.burgered.api.burger.ingredient.IngredientQuality

data class IngredientQualityComponent(
    val quality: IngredientQuality
) {
    companion object {
        val CODEC = RecordCodecBuilder.create { builder ->
            builder.group(
                IngredientQuality.CODEC.fieldOf("quality").forGetter(IngredientQualityComponent::quality)
            ).apply(builder, ::IngredientQualityComponent)
        }
    }
}