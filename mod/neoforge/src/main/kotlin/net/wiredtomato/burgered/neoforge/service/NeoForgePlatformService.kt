package net.wiredtomato.burgered.neoforge.service

import deplatformed.ServiceImpl
import net.neoforged.fml.ModList
import net.neoforged.fml.loading.FMLEnvironment
import net.neoforged.fml.loading.FMLPaths
import net.wiredtomato.burgered.service.PlatformService
import java.nio.file.Path

@ServiceImpl(PlatformService::class)
class NeoForgePlatformService : PlatformService {
    override fun platform(): String = "neoforge"
    override fun isModLoaded(modId: String): Boolean = ModList.get().isLoaded(modId)
    override fun isDevelopmentEnvironment(): Boolean = !FMLEnvironment.production
    override fun getConfigDir(): Path = FMLPaths.CONFIGDIR.get()
}