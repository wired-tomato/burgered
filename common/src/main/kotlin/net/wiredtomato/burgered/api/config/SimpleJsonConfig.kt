package net.wiredtomato.burgered.api.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.KProperty
import kotlin.reflect.full.declaredMemberProperties

class SimpleJsonConfig<T : Any>(
    blankConstructor: () -> T,
    val serializer: KSerializer<T>,
    pathSupplier: () -> Path,
) : ReadWriteProperty<Any, Any> {
    private var instance: T = blankConstructor()
    private val path = pathSupplier()
    private val json = Json {
        prettyPrint = true
        encodeDefaults = true
    }

    fun save(path: Path = this.path) {
        if (!Files.exists(path)) {
            Files.createDirectories(path.parent)
            Files.createFile(path)
        }

        val data = json.encodeToString(serializer, instance)
        Files.writeString(path, data, Charsets.UTF_8)
    }

    fun load(path: Path = this.path) {
        if (!Files.exists(path)) {
            save(path)
            return
        }

        val data = Files.readString(path, Charsets.UTF_8)
        instance = json.decodeFromString(serializer, data)
    }

    fun <V> getPropertyValue(propertyName: String): V {
        return getter<V>(propertyName)()
    }

    fun <V> getter(propertyName: String): () -> V {
        val property = getKProperty(instance::class, propertyName)
        return { property.getter.call(instance) as V }
    }

    fun <V> setPropertyValue(propertyName: String, value: V) {
        setter<V>(propertyName)(value)
    }

    fun <V> setter(propertyName: String): (V) -> Unit {
        val klass = instance::class
        val property = getKProperty(instance::class, propertyName)
        if (property !is KMutableProperty<*>) {
            throw IllegalArgumentException("Property with name: $propertyName is not mutable in $klass")
        }

        return { property.setter.call(instance, it) }
    }

    private fun getSubKProperty(vararg propertyNames: String): Pair<Any, KProperty<*>> {
        if (propertyNames.isEmpty()) throw IllegalArgumentException("Must specify at least 1 property")

        val subProps = propertyNames.toList().subList(0, propertyNames.size - 1)
        if (subProps.isEmpty()) return instance to getKProperty(instance::class, propertyNames.last())

        var currentObj: Any = instance
        subProps.forEach { name ->
            val property = getKProperty(currentObj::class, name)
            currentObj = property.getter.call(currentObj) ?: throw IllegalStateException("Property $name is null in ${currentObj::class}")
        }

        return currentObj to getKProperty(currentObj::class, propertyNames.last())
    }

    private fun getKProperty(klass: KClass<*>, propertyName: String): KProperty<*> {
        val property = klass.declaredMemberProperties.find { it.name == propertyName } ?: throw IllegalArgumentException("No property with name: $propertyName exists in $klass")
        return property
    }

    fun instance(): T = instance

    fun <R, V> getProperty(propertyName: String): ReadWriteProperty<R, V> {
        val prop = getKProperty(instance::class, propertyName)
        return backedReadWrite(instance, prop)
    }

    fun <R, V> getSubProperty(vararg propertyNames: String): ReadWriteProperty<R, V> {
        val prop = getSubKProperty(*propertyNames)
        return backedReadWrite(prop.first, prop.second)
    }

    private fun <R, V> backedReadWrite(obj: Any, prop: KProperty<*>): ReadWriteProperty<R, V> {
        return object: ReadWriteProperty<R, V> {
            override fun getValue(thisRef: R, property: KProperty<*>): V {
                return prop.getter.call(obj) as V
            }

            override fun setValue(thisRef: R, property: KProperty<*>, value: V) {
                if (prop !is KMutableProperty<*>) {
                    throw IllegalArgumentException("Property with name: ${prop.name} is not mutable in ${instance::class}")
                }
                prop.setter.call(obj, value)
            }
        }
    }

    override fun getValue(thisRef: Any, property: KProperty<*>): Any {
        return getKProperty(instance::class, property.name)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: Any) {
        setPropertyValue(property.name, value)
    }
}