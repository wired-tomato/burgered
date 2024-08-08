package net.wiredtomato.burgered.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.item.ItemStack
import net.wiredtomato.burgered.init.BurgeredItems

data class VanillaBurgerIngredientComponent(
    val stack: ItemStack,
    private var dirty: Boolean = true
) {
    fun isDirty() = dirty

    companion object {
        val CODEC: Codec<VanillaBurgerIngredientComponent> = RecordCodecBuilder.create { builder ->
            builder.group(
                ItemStack.CODEC.fieldOf("stack").orElse(ItemStack.EMPTY).forGetter(VanillaBurgerIngredientComponent::stack),
                Codec.BOOL.fieldOf("dirty").orElse(true).forGetter(VanillaBurgerIngredientComponent::dirty)
            ).apply(builder, ::VanillaBurgerIngredientComponent)
        }

        val DEFAULT by lazy { VanillaBurgerIngredientComponent(BurgeredItems.CUSTOM_BURGER_INGREDIENT.defaultInstance) }
    }
}
