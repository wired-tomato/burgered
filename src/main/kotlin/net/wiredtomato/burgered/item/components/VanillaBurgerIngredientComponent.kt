package net.wiredtomato.burgered.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.ItemStack
import net.wiredtomato.burgered.init.BurgeredItems

data class VanillaBurgerIngredientComponent(
    val stack: ItemStack,
    private var dirty: Boolean = true
) {
    fun isDirty() = dirty

    companion object {
        val CODEC: Codec<VanillaBurgerIngredientComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                ItemStack.CODEC.fieldOf("stack").orElse(BurgeredItems.CUSTOM_BURGER_INGREDIENT.defaultStack).forGetter(VanillaBurgerIngredientComponent::stack),
                Codec.BOOL.fieldOf("dirty").orElse(true).forGetter(VanillaBurgerIngredientComponent::dirty)
            ).apply(builder, ::VanillaBurgerIngredientComponent)
        }

        val DEFAULT = VanillaBurgerIngredientComponent(BurgeredItems.CUSTOM_BURGER_INGREDIENT.defaultStack)
    }
}
