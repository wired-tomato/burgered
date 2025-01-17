package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.tags.BlockTags
import net.neoforged.neoforge.common.data.BlockTagsProvider
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredBlocks
import java.util.concurrent.CompletableFuture

class BurgeredBlockTagProvider(
    output: PackOutput,
    lookupProvider: CompletableFuture<HolderLookup.Provider>
) : BlockTagsProvider(output, lookupProvider, Burgered.MOD_ID) {
    override fun addTags(arg: HolderLookup.Provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BurgeredBlocks.GRILL)

        tag(BlockTags.MINEABLE_WITH_AXE)
            .add(BurgeredBlocks.BURGER_STACKER)
    }
}