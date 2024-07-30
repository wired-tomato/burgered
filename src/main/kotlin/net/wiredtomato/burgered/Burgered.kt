package net.wiredtomato.burgered

import net.minecraft.util.Identifier
import net.wiredtomato.burgered.init.*
import org.slf4j.LoggerFactory

object Burgered {
    const val MOD_ID = "burgered"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    fun commonInit() {
        BurgeredBlocks
        BurgeredBlockEntities
        BurgeredItems
        BurgeredDataComponents
        BurgeredTabs
    }

    fun id(path: String) = Identifier.of(MOD_ID, path)
}