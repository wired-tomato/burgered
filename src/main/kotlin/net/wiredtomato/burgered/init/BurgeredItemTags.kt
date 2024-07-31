package net.wiredtomato.burgered.init

import net.minecraft.registry.RegistryKeys
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.util.tag

object BurgeredItemTags {
    val BURGER_STACKABLE = create("burger/stackable")
    fun create(path: String) = RegistryKeys.ITEM.tag(Burgered.id(path))
}