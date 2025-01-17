package net.wiredtomato.burgered.fabric.service

import deplatformed.ServiceImpl
import net.fabricmc.loader.api.FabricLoader
import net.wiredtomato.burgered.service.PlatformService
import java.nio.file.Path

@ServiceImpl(PlatformService::class)
class FabricPlatformService : PlatformService {
    override fun platform(): String = "fabric"
    override fun isModLoaded(modId: String): Boolean = FabricLoader.getInstance().isModLoaded(modId)
    override fun isDevelopmentEnvironment(): Boolean = FabricLoader.getInstance().isDevelopmentEnvironment
    override fun getConfigDir(): Path = FabricLoader.getInstance().configDir
}