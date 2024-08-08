package net.wiredtomato.burgered.api.data.gen

import com.google.gson.JsonElement
import com.mojang.serialization.DynamicOps
import com.mojang.serialization.JsonOps
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.CachedOutput
import net.minecraft.data.DataProvider
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.wiredtomato.burgered.api.data.burger.BurgerStackable
import java.util.concurrent.CompletableFuture

abstract class BurgerStackableProvider(
    output: PackOutput,
    private val registryProvider: CompletableFuture<HolderLookup.Provider>,
    val modId: String
) : DataProvider {
    private val pathProvider = output.createPathProvider(PackOutput.Target.DATA_PACK, "burgered/stackables")

    abstract fun buildStackables(builder: StackableBuilder, lookup: HolderLookup.Provider)

    override fun run(cachedOutput: CachedOutput): CompletableFuture<*> {
        return registryProvider.thenCompose { lookup ->
            val entries = mutableMapOf<ResourceLocation, JsonElement>()
            val ops = lookup.createSerializationContext(JsonOps.INSTANCE)

            val builder = object : StackableBuilder {
                override val modId: String = this@BurgerStackableProvider.modId

                override fun offer(
                    stackable: BurgerStackable,
                    location: ResourceLocation
                ) {
                    val json = toJson(location, stackable, ops)
                    val existing = entries.put(location, json)
                    if (existing != null) throw IllegalArgumentException("Duplicate entry for $location")
                }
            }

            buildStackables(builder, lookup)
            writeAll(cachedOutput, entries)
        }
    }

    interface StackableBuilder {
        val modId: String

        fun offer(stackable: BurgerStackable, location: ResourceLocation)
        fun offer(stackable: BurgerStackable) = offer(stackable, ResourceLocation.fromNamespaceAndPath(modId, BuiltInRegistries.ITEM.getKey(stackable.item).path))
    }

    private fun toJson(location: ResourceLocation, stackable: BurgerStackable, ops: DynamicOps<JsonElement>): JsonElement {
        val result = BurgerStackable.CODEC.encodeStart(ops, stackable)
        return result
            .mapError { "Invalid entry $location: $it" }
            .getOrThrow()
    }

    private fun writeAll(out: CachedOutput, entries: Map<ResourceLocation, JsonElement>): CompletableFuture<*> {
        return CompletableFuture.allOf(
            *entries.map { (location, stackable) ->
                val path = pathProvider.json(location)
                DataProvider.saveStable(out, stackable, path)
            }.toTypedArray()
        )
    }

    override fun getName() = "Burger Stackable Provider fir $modId"
}
