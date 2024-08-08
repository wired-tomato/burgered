package net.wiredtomato.burgered.neoforge

import net.neoforged.fml.common.Mod
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent
import net.neoforged.neoforge.client.gui.IConfigScreenFactory
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.client.config.BurgeredClientConfig
import net.wiredtomato.burgered.neoforge.client.BurgeredNeoforgeClient
import thedarkcolour.kotlinforforge.neoforge.forge.LOADING_CONTEXT
import thedarkcolour.kotlinforforge.neoforge.forge.MOD_BUS
import thedarkcolour.kotlinforforge.neoforge.forge.runForDist

@Mod(Burgered.MOD_ID)
object BurgeredNeoforge {
    init {
        Burgered.init()

        runForDist(
            clientTarget = {
                MOD_BUS.addListener(BurgeredNeoforgeClient::clientSetup)
            },
            serverTarget = {
                MOD_BUS.addListener(BurgeredNeoforge::serverSetup)
            }
        )

        LOADING_CONTEXT.registerExtensionPoint(IConfigScreenFactory::class.java) {
            IConfigScreenFactory { client, parent -> BurgeredClientConfig.createScreen(parent) }
        }
    }

    fun serverSetup(event: FMLDedicatedServerSetupEvent) {}
}
