package net.wiredtomato.burgered.service.client

import net.minecraft.client.gui.screens.Screen
import net.wiredtomato.burgered.service.Services

interface ConfigService {
    fun createConfigScreen(parentScreen: Screen?): Screen
}

val ConfigServiceImpl = Services.getService<ConfigService>()
