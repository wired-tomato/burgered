package net.wiredtomato.burgered.service

import com.mojang.serialization.Codec
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.wiredtomato.burgered.api.function.KSupplier

interface RegistryService {
    fun <T : Any> createDynamicRegistry(key: ResourceKey<Registry<T>>, codec: Codec<T>, networkCodec: Codec<T> = codec)
    fun <T : Any> createRegistry(key: ResourceKey<Registry<T>>, synced: Boolean = false, defaultKey: ResourceLocation? = null, maxId: Int? = null): Registry<T>
    fun <T : Any, V : T> register(registry: Registry<T>, id: ResourceLocation, value: KSupplier<V>): KSupplier<V>
}

val RegistryServiceImpl = Services.getService<RegistryService>()
