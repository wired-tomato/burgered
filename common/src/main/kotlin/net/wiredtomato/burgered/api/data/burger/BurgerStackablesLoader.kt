package net.wiredtomato.burgered.api.data.burger

import com.google.gson.JsonParser
import com.mojang.serialization.JsonOps
import net.minecraft.resources.FileToIdConverter
import net.minecraft.server.packs.resources.ResourceManager
import net.minecraft.server.packs.resources.ResourceManagerReloadListener
import net.wiredtomato.burgered.Burgered
import java.nio.charset.StandardCharsets

object BurgerStackablesLoader : ResourceManagerReloadListener {
    val STACKABLES_IDS = FileToIdConverter.json("${Burgered.MOD_ID}/stackables")

    override fun onResourceManagerReload(manager: ResourceManager) {
        BurgerStackables.clear()
        STACKABLES_IDS.listMatchingResources(manager).forEach { (id, resource) ->
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