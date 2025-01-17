package net.wiredtomato.burgered

import net.minecraft.resources.ResourceLocation
import net.wiredtomato.burgered.api.registry.BurgeredRegistries
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.init.BurgeredBlocks
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.init.BurgeredRecipeBookCategories
import net.wiredtomato.burgered.init.BurgeredRecipes
import net.wiredtomato.burgered.init.BurgeredTabs
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import net.wiredtomato.burgered.service.PlatformServiceImpl

object Burgered {
    const val MOD_ID = "burgered"
    @JvmStatic
    val LOGGER: Logger = LoggerFactory.getLogger(MOD_ID)

    fun commonInit() {
        LOGGER.info("Loading $MOD_ID for ${PlatformServiceImpl.platform()}")
        BurgeredRegistries
        BurgeredBlocks
        BurgeredBlockEntities
        BurgeredDataComponents
        BurgeredItems
        BurgeredRecipeBookCategories
        BurgeredRecipes
        BurgeredRecipes.Serializers
        BurgeredTabs
    }

    fun modLoc(path: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}