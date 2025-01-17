package net.wiredtomato.burgered.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.network.chat.Component
import net.minecraft.world.item.CreativeModeTab
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.service.CreativeModeTabBuilderImpl
import net.wiredtomato.burgered.service.RegistryServiceImpl
import java.util.function.Consumer

object BurgeredTabs {
    val BURGERED_TAB by registering("burgered_tab") { builder ->
        builder.title(Component.translatable("itemGroup.burgered.burgered_tab"))
            .icon { BurgeredItems.BURGER.defaultInstance }
            .displayItems { _, entries ->
                BurgeredItems.items().forEach {
                    entries.accept(it)
                }
            }
    }


    fun registering(
        name: String,
        tabBuilder: Consumer<CreativeModeTab.Builder>
    ): KSupplier<CreativeModeTab> {
        return RegistryServiceImpl.register(BuiltInRegistries.CREATIVE_MODE_TAB, Burgered.modLoc(name)) {
            CreativeModeTabBuilderImpl.createBuilder().also { tabBuilder.accept(it) }.build()
        }
    }
}