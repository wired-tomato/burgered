package net.wiredtomato.burgered

import net.minecraft.util.Identifier
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.init.BurgeredBlocks
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import org.slf4j.LoggerFactory

object Burgered {
    const val MOD_ID = "burgered"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    fun commonInit() {
        BurgeredBlocks
        BurgeredBlockEntities
        BurgeredItems
        BurgeredDataComponents
    }

    fun id(path: String) = Identifier.of(MOD_ID, path)
}