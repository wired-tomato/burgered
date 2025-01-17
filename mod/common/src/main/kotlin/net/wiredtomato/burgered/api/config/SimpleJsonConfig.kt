package net.wiredtomato.burgered.api.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import java.nio.file.Files
import java.nio.file.Path

class SimpleJsonConfig<T : Any>(
    blankConstructor: () -> T,
    val serializer: KSerializer<T>,
    pathSupplier: () -> Path,
) {
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

    fun instance(): T = instance
}