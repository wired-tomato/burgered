package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.minecraft.item.Items
import net.minecraft.registry.HolderLookup
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.data.gen.BurgerStackableProvider
import net.wiredtomato.burgered.data.burger.BurgerStackable
import java.util.concurrent.CompletableFuture

class BurgeredStackableProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>
) : BurgerStackableProvider(dataOutput, registryLookup, Burgered.MOD_ID) {
    override fun configure(lookup: HolderLookup.Provider, builder: StackablesBuilder) {
        builder.offer(
            BurgerStackable(
                Items.NETHER_WART,
                2,
                4f
            )
        )

        builder.offer(
            BurgerStackable(
                Items.ENDER_PEARL,
                4,
                8f,
                listOf()
            )
        )
    }
}