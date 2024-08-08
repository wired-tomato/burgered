package net.wiredtomato.burgered.api.registry

import dev.architectury.registry.registries.RegistrySupplier
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class RegistryDelegate<T, V>(
    val registrySupplier: RegistrySupplier<V>
) : ReadOnlyProperty<T, V> {
    override fun getValue(thisRef: T, property: KProperty<*>): V {
        return registrySupplier.value()
    }
}

fun <T, V> T.registered(supplier: RegistrySupplier<V>) = RegistryDelegate<T, V>(supplier)
