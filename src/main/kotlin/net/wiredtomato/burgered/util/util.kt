package net.wiredtomato.burgered.util

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapCodec.MapCodecCodec
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import net.minecraft.nbt.NbtElement
import net.minecraft.nbt.NbtNull
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.NbtTagSizeTracker
import net.minecraft.network.PacketByteBuf
import net.minecraft.network.RegistryByteBuf
import net.minecraft.network.codec.PacketCodec
import net.minecraft.registry.Registry
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.tag.TagKey
import net.minecraft.util.Identifier
import java.util.function.Supplier

fun <T, R : Registry<T>> RegistryKey<R>.tag(id: Identifier) = TagKey.of(this, id)

fun <T> List<T>.group(): List<Group<T>> {
    val groups = mutableListOf<Group<T>>()
    forEach {
        val last = groups.lastOrNull()
        if (last != null && last.value == it) {
            last.count++
        } else {
            groups.add(Group(it, 1))
        }
    }
    return groups
}

fun <T, V> List<T>.createGroupsBy(transform: (T) -> V): List<Group<T>> {
    val groups = mutableListOf<Group<T>>()
    forEach {
        val last = groups.lastOrNull()
        if (last != null && transform(last.value) == transform(it)) {
            last.count++
        } else {
            groups.add(Group(it, 1))
        }
    }
    return groups
}

fun <T> MapCodec<T>.packetCodec(): PacketCodec<RegistryByteBuf, T> {
    return fromCodec(MapCodecCodec(this)) {
        NbtTagSizeTracker.create(2097152L)
    }
}

fun <T> fromCodec(codec: Codec<T>, sizeTracker: Supplier<NbtTagSizeTracker>): PacketCodec<RegistryByteBuf, T> {
    return nbtElement(sizeTracker).map(
        { element: NbtElement ->
            codec.parse(NbtOps.INSTANCE, element).getOrThrow { string: String ->
                DecoderException(
                    "Failed to decode: $string $element"
                )
            }
        },
        { value: T ->
            codec.encodeStart(NbtOps.INSTANCE, value).getOrThrow { string: String ->
                EncoderException(
                    "Failed to encode: " + string + " " + value.toString()
                )
            } as NbtElement
        })
}

fun nbtElement(sizeTracker: Supplier<NbtTagSizeTracker>): PacketCodec<RegistryByteBuf, NbtElement> {
    return object : PacketCodec<RegistryByteBuf, NbtElement> {
        override fun decode(byteBuf: RegistryByteBuf): NbtElement {
            val nbtElement = PacketByteBuf.readNbt(byteBuf, sizeTracker.get())
            if (nbtElement == null) {
                throw DecoderException("Expected non-null compound tag")
            } else {
                return nbtElement
            }
        }

        override fun encode(byteBuf: RegistryByteBuf, nbtElement: NbtElement) {
            if (nbtElement === NbtNull.INSTANCE) {
                throw EncoderException("Expected non-null compound tag")
            } else {
                PacketByteBuf.writeNbt(byteBuf, nbtElement)
            }
        }
    }
}

data class Group<T>(val value: T, var count: Int)
