package net.wiredtomato.burgered.util

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.MapCodec.MapCodecCodec
import io.netty.handler.codec.DecoderException
import io.netty.handler.codec.EncoderException
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.nbt.*
import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.world.item.Item
import java.util.function.Supplier

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

fun <T> MapCodec<T>.packetCodec(): StreamCodec<RegistryFriendlyByteBuf, T> {
    return fromCodec(MapCodecCodec(this)) {
        NbtAccounter.create(2097152L)
    }
}

fun <T> fromCodec(codec: Codec<T>, sizeTracker: Supplier<NbtAccounter>): StreamCodec<RegistryFriendlyByteBuf, T> {
    return nbtElement(sizeTracker).map(
        { element: Tag ->
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
            } as Tag
        })
}

fun nbtElement(sizeTracker: Supplier<NbtAccounter>): StreamCodec<RegistryFriendlyByteBuf, Tag> {
    return object : StreamCodec<RegistryFriendlyByteBuf, Tag> {
        override fun decode(byteBuf: RegistryFriendlyByteBuf): Tag {
            val nbtElement = FriendlyByteBuf.readNbt(byteBuf, sizeTracker.get())
            if (nbtElement == null) {
                throw DecoderException("Expected non-null compound tag")
            } else {
                return nbtElement
            }
        }

        override fun encode(byteBuf: RegistryFriendlyByteBuf, nbtElement: Tag) {
            if (nbtElement === EndTag.INSTANCE) {
                throw EncoderException("Expected non-null compound tag")
            } else {
                FriendlyByteBuf.writeNbt(byteBuf, nbtElement)
            }
        }
    }
}

data class Group<T>(val value: T, var count: Int)

val Item.id get() = BuiltInRegistries.ITEM.getKey(this)
