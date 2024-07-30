package net.wiredtomato.burgered.util

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

data class Group<T>(val value: T, var count: Int)
