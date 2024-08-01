package net.wiredtomato.burgered.client.compat.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import eu.midnightdust.lib.config.MidnightConfig
import net.wiredtomato.burgered.Burgered

class ModMenuEntrypoint : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> = ConfigScreenFactory { parent ->
        MidnightConfig.getScreen(parent, Burgered.MOD_ID)
    }
}