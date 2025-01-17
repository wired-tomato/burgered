package net.wiredtomato.burgered.fabric.service

import com.mojang.serialization.Codec
import deplatformed.ServiceImpl
import net.fabricmc.fabric.api.event.registry.DynamicRegistries
import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder
import net.fabricmc.fabric.api.event.registry.RegistryAttribute
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.service.RegistryService

@ServiceImpl(RegistryService::class)
class FabricRegistryService : RegistryService {
    override fun <T : Any> createDynamicRegistry(
        key: ResourceKey<Registry<T>>,
        codec: Codec<T>,
        networkCodec: Codec<T>
    ) {
        DynamicRegistries.registerSynced(key, codec, networkCodec)
    }

    override fun <T : Any> createRegistry(
        key: ResourceKey<Registry<T>>,
        synced: Boolean,
        defaultKey: ResourceLocation?,
        maxId: Int?
    ): Registry<T> {
        return (if (defaultKey != null) FabricRegistryBuilder.createDefaulted(key, defaultKey) else FabricRegistryBuilder.createSimple(key))
            .also { if (synced) it.attribute(RegistryAttribute.SYNCED) }
            .buildAndRegister()
    }

    override fun <T : Any, V : T> register(registry: Registry<T>, id: ResourceLocation, value: KSupplier<V>): KSupplier<V> {
        val registered = Registry.register(registry, id, value.get())
        return KSupplier { registered }
    }
}