package net.wiredtomato.burgered.service

import java.nio.file.Path

interface PlatformService {
    fun platform(): String
    fun isModLoaded(modId: String): Boolean
    fun isDevelopmentEnvironment(): Boolean
    fun getEnvironmentName(): String = if (isDevelopmentEnvironment()) "development" else "production"
    fun getConfigDir(): Path
}

val PlatformServiceImpl = Services.getService<PlatformService>()
