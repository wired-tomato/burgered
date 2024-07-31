package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.RegistryKeys
import net.wiredtomato.burgered.init.BurgeredItemTags
import net.wiredtomato.burgered.init.BurgeredItems
import java.util.concurrent.CompletableFuture

class BurgeredItemTagProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricTagProvider<Item>(output, RegistryKeys.ITEM, registriesFuture) {
    override fun configure(wrapperLookup: HolderLookup.Provider) {
        getOrCreateTagBuilder(BurgeredItemTags.BURGER_STACKABLE)
            .add(BurgeredItems.ESTROGEN_WAFFLE)
            .add(Items.ENDER_PEARL)
            .add(Items.NETHER_BRICK)
            .add(Items.NETHER_WART)
            .add(Items.NETHER_STAR)
            .add(Items.DEAD_BUSH)
            .add(Items.NETHERITE_INGOT)
    }
}