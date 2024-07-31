package net.wiredtomato.burgered.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.wiredtomato.burgered.init.BurgeredItems

data class VanillaBurgerIngredientComponent(
    val item: Item
) {

    companion object {
        val CODEC: Codec<VanillaBurgerIngredientComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                Registries.ITEM.codec.fieldOf("item").forGetter(VanillaBurgerIngredientComponent::item)
            ).apply(builder, ::VanillaBurgerIngredientComponent)
        }

        val DEFAULT = VanillaBurgerIngredientComponent(BurgeredItems.CUSTOM_BURGER_INGREDIENT)
    }
}
