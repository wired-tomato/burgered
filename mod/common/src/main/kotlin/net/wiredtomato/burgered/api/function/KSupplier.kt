package net.wiredtomato.burgered.api.function

import java.util.function.Supplier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

fun interface KSupplier<T> : Supplier<T>, ReadOnlyProperty<Any?, T> {
    override fun get(): T
    operator fun invoke() = get()
    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return get()
    }

    @Suppress("UNCHECKED_CAST")
    fun <V : T> cast(): KSupplier<V> {
        return KSupplier { get() as V }
    }
}

fun <T> Supplier<T>.k() = KSupplier { get() }

fun <T> Iterable<KSupplier<T>>.obtain(): List<T> {
    return map { it() }
}
