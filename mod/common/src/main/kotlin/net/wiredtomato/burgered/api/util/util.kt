package net.wiredtomato.burgered.api.util

import net.minecraft.core.Holder
import net.minecraft.core.component.DataComponentPatch
import net.minecraft.core.component.PatchedDataComponentMap
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack

fun ItemStack.withPatch(patch: DataComponentPatch): ItemStack {
    val copy = copy()
    val components = copy.components
    if (components is PatchedDataComponentMap) {
        components.applyPatch(patch)
    }

    return copy
}

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

@Suppress("UNCHECKED_CAST")
inline fun <T, reified V> Holder<T>.cast(): Holder<V> {
    val value = value()
    if (value is V) {
        return this as Holder<V>
    } else {
        throw ClassCastException("Expected $this to be of type ${V::class}")
    }
}

data class Group<T>(val value: T, var count: Int)

val Item.id get() = BuiltInRegistries.ITEM.getKey(this)
