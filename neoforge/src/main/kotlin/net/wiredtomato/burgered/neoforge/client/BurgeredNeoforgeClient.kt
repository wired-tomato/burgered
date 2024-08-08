package net.wiredtomato.burgered.neoforge.client

import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent
import net.wiredtomato.burgered.client.BurgeredClient

object BurgeredNeoforgeClient {
    fun clientSetup(event: FMLClientSetupEvent) {
        BurgeredClient.init()
    }
}
