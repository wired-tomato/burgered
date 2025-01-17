package net.wiredtomato.burgered.neoforge

import net.neoforged.api.distmarker.Dist
import net.neoforged.fml.common.Mod
import net.neoforged.neoforge.client.event.RegisterSpecialModelRendererEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.event.client.ClientEvents
import net.wiredtomato.burgered.client.BurgeredClient
import net.wiredtomato.burgered.client.config.BurgeredClientConfig
import net.wiredtomato.burgered.neoforge.service.NeoForgeRegistryService
import net.wiredtomato.burgered.neoforge.service.client.NeoForgeBlockEntityRenderingService
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runWhenOn

@Mod(Burgered.MOD_ID)
object NeoForgeBurgered {
    init {
        Burgered.commonInit()
        MOD_BUS.addListener(NeoForgeRegistryService.Companion::registerDynamicRegistries)
        MOD_BUS.addListener(NeoForgeRegistryService.Companion::registerRegistries)
        MOD_BUS.addListener(NeoForgeBlockEntityRenderingService.Companion::registerEntityRenderers)
        NeoForgeRegistryService.Companion.registerDeferred()

        runWhenOn(Dist.CLIENT) {
            BurgeredClient.init()
            MOD_BUS.addListener(::registerSpecialRenderers)
        }

        LOADING_CONTEXT.registerExtensionPoint(IConfigScreenFactory::class.java) {
            IConfigScreenFactory { client, parent -> BurgeredClientConfig.createScreen(parent) }
        }
    }

    fun registerSpecialRenderers(event: RegisterSpecialModelRendererEvent) {
        ClientEvents.REGISTER_SPECIAL_RENDERERS.invoker().registerRenderers(ClientEvents.RegisterSpecialRenderers.Registry(event::register))
    }
}