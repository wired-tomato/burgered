package net.wiredtomato.burgered.api.data.burger

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.wiredtomato.burgered.api.StatusEffectEntry
import net.wiredtomato.burgered.api.event.LivingEntityEvents
import net.wiredtomato.burgered.init.BurgeredRegistries
import net.wiredtomato.burgered.util.byNameCodec
import org.jetbrains.annotations.ApiStatus.Internal
import java.util.*

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
                BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(BurgerStackable::item),
                Codec.INT.fieldOf("hunger").orElse(0).forGetter(BurgerStackable::hunger),
                Codec.FLOAT.fieldOf("saturation").orElse(0f).forGetter(BurgerStackable::saturation),
                Codec.DOUBLE.fieldOf("modelHeight").orElse(1.0).forGetter(BurgerStackable::modelHeight),
                StatusEffectEntry.CODEC.listOf().fieldOf("statusEffects").orElse(listOf()).forGetter(BurgerStackable::statusEffects),
                Codec.STRING.optionalFieldOf("customName").forGetter(BurgerStackable::customName),
                BurgeredRegistries.EAT_EVENT.byNameCodec().optionalFieldOf("eatEvent").forGetter(BurgerStackable::eatEvent),
            ).apply(builder, ::BurgerStackable)
        }
    }
}

fun interface BurgerStackableEatCallback : LivingEntityEvents.EatCallback

@Internal object BurgerStackables : MutableList<BurgerStackable> by mutableListOf()
