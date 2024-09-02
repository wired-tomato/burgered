package net.wiredtomato.burgered

import dev.architectury.event.events.common.LifecycleEvent
import dev.architectury.event.events.common.PlayerEvent
import dev.architectury.networking.NetworkManager
import dev.architectury.registry.ReloadListenerRegistry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.wiredtomato.burgered.api.data.burger.BurgerStackables
import net.wiredtomato.burgered.api.data.burger.BurgerStackablesLoader
import net.wiredtomato.burgered.api.event.LivingEntityEvents
import net.wiredtomato.burgered.api.ingredient.ingredient
import net.wiredtomato.burgered.init.*
import net.wiredtomato.burgered.networking.StackableSyncPacket
import org.slf4j.LoggerFactory

object Burgered {
    const val MOD_ID = "burgered"
    val LOGGER = LoggerFactory.getLogger(Burgered::class.java)

    fun init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, BurgerStackablesLoader)
        BurgeredRegistries
        BurgeredBlocks.BLOCKS.register()
        BurgeredBlockEntities.BLOCK_ENTITIES.register()
        BurgeredDataComponents.DATA_COMPONENTS.register()
        BurgeredItems.ITEMS.register()
        BurgeredRecipes.RECIPES.register()
        BurgeredRecipes.Serializers.RECIPE_SERIALIZERS.register()
        BurgeredTabs.TABS.register()
        BurgeredEatEvents.EAT_EVENTS.register()

        LifecycleEvent.SERVER_BEFORE_START.register {
            if (it.isDedicatedServer) {
                NetworkManager.registerS2CPayloadType(StackableSyncPacket.TYPE, StackableSyncPacket.PACKET_CODEC)
            }
        }

        PlayerEvent.PLAYER_JOIN.register { player ->
            val payload = StackableSyncPacket(BurgerStackables.toList())
            NetworkManager.sendToPlayer(player, payload)
        }

        LivingEntityEvents.ON_EAT.register onEat@ { entity, world, stack, component ->
            val burger = stack.get(BurgeredDataComponents.BURGER) ?: run {
                val item = stack.item
                item.ingredient()?.onEat(entity, world, stack, component)

                return@onEat
            }
            burger.onEat(entity, world, stack, component)
        }
    }

    fun modLoc(path: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}
