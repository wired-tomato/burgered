package net.wiredtomato.burgered.data.burger

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener
import net.minecraft.registry.ResourceFileNamespace
import net.minecraft.resource.ResourceManager
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.Burgered
import java.nio.charset.StandardCharsets

object BurgerStackablesLoader : SimpleSynchronousResourceReloadListener {
    val STACKABLES_IDS = ResourceFileNamespace.json("${Burgered.MOD_ID}/stackables")

    override fun reload(manager: ResourceManager) {
        BurgerStackables.clear()
        STACKABLES_IDS.findAllMatchingResources(manager).forEach { (id, resources) ->
            resources.forEach { resource ->
                resource.open().use { stream ->
                    val element = JsonParser.parseReader(stream.bufferedReader(StandardCharsets.UTF_8))
                    val stackable = BurgerStackable.CODEC.decode(JsonOps.INSTANCE, element)
                    stackable.ifError {
                        throw IllegalStateException("Failed to parse burger stackable: $id")
                    }

                    stackable.ifSuccess { pair ->
                        BurgerStackables.add(pair.first)
                    }
                }
            }
        }
    }

    override fun getFabricId(): Identifier = Burgered.id("burger_stackables_loader")
}