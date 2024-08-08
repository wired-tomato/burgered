package net.wiredtomato.burgered.util

import com.mojang.serialization.Codec
import com.mojang.serialization.DataResult
import dev.architectury.registry.registries.Registrar
import net.minecraft.core.Holder
import net.minecraft.resources.ResourceLocation
import java.util.*

fun <T> Registrar<T>.byNameCodec(): Codec<T> {
    return byNameHolderCodec().comapFlatMap({ holder ->
        holder.unwrap().right().map { DataResult.success(it) }.orElseGet {
            DataResult.error {
                val registry = key().toString()
                "Unknown registry key in $registry: ${holder.unwrapKey().get().location()}"
            }
        }
    }) { t ->
        getHolder(getId(t))
    }
}

fun <T> Registrar<T>.byNameHolderCodec(): Codec<Holder<T>> {
    return ResourceLocation.CODEC.comapFlatMap({ location ->
        Optional.ofNullable(this.getHolder(location)).map { DataResult.success(it) }.orElseGet {
            DataResult.error {
                val registry = key().toString()
                "Unknown registry key in $registry: $location"
            }
        }
    }) { holder ->
        holder.unwrapKey().get().location()
    }
}
