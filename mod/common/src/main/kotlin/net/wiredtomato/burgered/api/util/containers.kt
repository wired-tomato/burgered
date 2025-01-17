package net.wiredtomato.burgered.api.util

import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.nbt.ListTag
import net.minecraft.nbt.NbtOps
import net.minecraft.nbt.Tag
import net.minecraft.world.item.ItemStack

fun NonNullList<ItemStack>.save(compound: CompoundTag) {
    val list = ListTag()
    val ops = NbtOps.INSTANCE
    val codec = ItemStack.OPTIONAL_CODEC
    forEach { stack ->
        val encoded = codec.encode(stack, ops, CompoundTag())
        encoded.ifError { error ->
            throw IllegalStateException("Failed to encode $stack: ${error.message()}")
        }.ifSuccess { stackTag ->
            list.add(stackTag)
        }
    }

    compound.put("inventory", list)
}

fun NonNullList<ItemStack>.load(compound: CompoundTag) {
    val list = compound.getList("inventory", Tag.TAG_COMPOUND.toInt())
    val ops = NbtOps.INSTANCE
    val codec = ItemStack.OPTIONAL_CODEC

    (0..<this.size).forEach { i ->
        val stackTag = list.getOrNull(i) ?: return@forEach
        val stackResult = codec.decode(ops, stackTag)
        stackResult.ifError { error ->
            throw IllegalStateException("Failed to decode $stackTag: ${error.message()}")
        }.ifSuccess { stack ->
            this[i] = stack.first
        }
    }
}
