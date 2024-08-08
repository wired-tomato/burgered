package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.world.item.Items
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.data.burger.BurgerStackable
import net.wiredtomato.burgered.api.data.gen.BurgerStackableProvider
import java.util.concurrent.CompletableFuture

class BurgeredStackablesProvider(
    output: PackOutput,
    registryProvider: CompletableFuture<HolderLookup.Provider>
) : BurgerStackableProvider(output, registryProvider, Burgered.MOD_ID) {
    override fun buildStackables(
        builder: StackableBuilder,
        lookup: HolderLookup.Provider
    ) {
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
                8f
            )
        )
    }
}