package net.wiredtomato.burgered.fabric.modmenu

import com.terraformersmc.modmenu.api.ConfigScreenFactory
import com.terraformersmc.modmenu.api.ModMenuApi
import net.wiredtomato.burgered.client.config.BurgeredClientConfig

object BurgeredModMenu : ModMenuApi {
    override fun getModConfigScreenFactory(): ConfigScreenFactory<*> {
        return ConfigScreenFactory { parent -> BurgeredClientConfig.createScreen(parent) }
    }
}
