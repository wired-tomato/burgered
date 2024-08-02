package net.wiredtomato.burgered.data.burger

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.item.FoodComponent.StatusEffectEntry
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.wiredtomato.burgered.api.event.LivingEntityEvents
import net.wiredtomato.burgered.init.BurgeredRegistries
import org.jetbrains.annotations.ApiStatus.Internal
import java.util.Optional

data class BurgerStackable(
    val item: Item,
    val hunger: Int,
    val saturation: Float,
    val modelHeight: Double = 1.0,
    val statusEffects: List<StatusEffectEntry> = listOf(),
    val customName: Optional<String> = Optional.empty(),
    val eatEvent: Optional<BurgerStackableEatCallback> = Optional.empty()
) {
    companion object {
        val CODEC: Codec<BurgerStackable> = RecordCodecBuilder.create { builder ->
            builder.group(
                Registries.ITEM.codec.fieldOf("item").forGetter(BurgerStackable::item),
                Codec.INT.fieldOf("hunger").orElse(0).forGetter(BurgerStackable::hunger),
                Codec.FLOAT.fieldOf("saturation").orElse(0f).forGetter(BurgerStackable::saturation),
                Codec.DOUBLE.fieldOf("modelHeight").orElse(1.0).forGetter(BurgerStackable::modelHeight),
                StatusEffectEntry.CODEC.listOf().fieldOf("statusEffects").orElse(listOf()).forGetter(BurgerStackable::statusEffects),
                Codec.STRING.optionalFieldOf("customName").forGetter(BurgerStackable::customName),
                BurgeredRegistries.EAT_EVENT.codec.optionalFieldOf("eatEvent").forGetter(BurgerStackable::eatEvent),
            ).apply(builder, ::BurgerStackable)
        }
    }
}

fun interface BurgerStackableEatCallback : LivingEntityEvents.EatCallback

@Internal object BurgerStackables : MutableList<BurgerStackable> by mutableListOf()
