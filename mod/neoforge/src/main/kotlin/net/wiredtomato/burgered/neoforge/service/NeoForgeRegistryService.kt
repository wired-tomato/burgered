package net.wiredtomato.burgered.neoforge.service

import com.mojang.serialization.Codec
import deplatformed.ServiceImpl
import net.minecraft.core.Registry
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.neoforged.neoforge.registries.DataPackRegistryEvent
import net.neoforged.neoforge.registries.DeferredRegister
import net.neoforged.neoforge.registries.NewRegistryEvent
import net.neoforged.neoforge.registries.RegistryBuilder
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.api.function.k
import net.wiredtomato.burgered.service.RegistryService
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS

@ServiceImpl(RegistryService::class)
class NeoForgeRegistryService : RegistryService {
    override fun <T : Any> createDynamicRegistry(
        key: ResourceKey<Registry<T>>,
        codec: Codec<T>,
        networkCodec: Codec<T>
    ) {
        dataPackRegistries.add(DataPackRegistry(key, codec, networkCodec))
    }

    override fun <T : Any> createRegistry(
        key: ResourceKey<Registry<T>>,
        synced: Boolean,
        defaultKey: ResourceLocation?,
        maxId: Int?
    ): Registry<T> {
        return RegistryBuilder(key).also {
            it.sync(synced)
            if (defaultKey != null) it.defaultKey(defaultKey)
            if (maxId != null) it.maxId(maxId)
        }.create().also { registries.add(it) }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : Any, V : T> register(registry: Registry<T>, id: ResourceLocation, value: KSupplier<V>): KSupplier<V> {
        val register = deferredRegisters.computeIfAbsent(registry to id.namespace) {
            DeferredRegister.create(registry, id.namespace)
        } as DeferredRegister<T>

        return register.register(id.path, value).k()
    }

    companion object {
        private val dataPackRegistries = mutableListOf<DataPackRegistry<*>>()
        private val registries = mutableListOf<Registry<*>>()
        private val deferredRegisters = mutableMapOf<Pair<Registry<*>, String>, DeferredRegister<*>>()

        fun registerDynamicRegistries(event: DataPackRegistryEvent.NewRegistry) {
            fun <T> DataPackRegistry<T>.register(event: DataPackRegistryEvent.NewRegistry) {
                event.dataPackRegistry(this.key, this.codec, this.networkCodec)
            }

            dataPackRegistries.forEach { it.register(event) }
        }

        fun registerRegistries(event: NewRegistryEvent) {
            registries.forEach { event.register(it) }
        }

        fun registerDeferred() {
            deferredRegisters.forEach { (regModId, register) ->
                Burgered.LOGGER.info("Registering ${register.registryName} for ${regModId.second}")
                register.register(MOD_BUS)
            }
        }

        private class DataPackRegistry<T>(
            val key: ResourceKey<Registry<T>>,
            val codec: Codec<T>,
            val networkCodec: Codec<T>
        )
    }
}