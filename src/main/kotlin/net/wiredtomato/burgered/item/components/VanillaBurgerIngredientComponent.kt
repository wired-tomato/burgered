package net.wiredtomato.burgered.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.wiredtomato.burgered.init.BurgeredItems

data class VanillaBurgerIngredientComponent(
    val item: Item,
    private var dirty: Boolean = true
) {
    fun isDirty() = dirty

    companion object {
        val CODEC: Codec<VanillaBurgerIngredientComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                Registries.ITEM.codec.fieldOf("item").forGetter(VanillaBurgerIngredientComponent::item),
                Codec.BOOL.fieldOf("dirty").orElse(true).forGetter(VanillaBurgerIngredientComponent::dirty)
            ).apply(builder, ::VanillaBurgerIngredientComponent)
        }

        val DEFAULT = VanillaBurgerIngredientComponent(BurgeredItems.CUSTOM_BURGER_INGREDIENT)
    }
}
