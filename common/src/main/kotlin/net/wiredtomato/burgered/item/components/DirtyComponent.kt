package net.wiredtomato.burgered.item.components

import com.mojang.serialization.Codec
import com.mojang.serialization.codecs.RecordCodecBuilder

data class DirtyComponent(
    var dirty: Boolean = true
) {
    companion object {
        val CODEC = RecordCodecBuilder.create { builder ->
            builder.group(
                Codec.BOOL.fieldOf("dirty").orElse(true).forGetter(DirtyComponent::dirty)
            ).apply(builder, ::DirtyComponent)
        }
    }
}
