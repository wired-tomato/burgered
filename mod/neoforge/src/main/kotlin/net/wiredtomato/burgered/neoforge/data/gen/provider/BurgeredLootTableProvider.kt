package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.core.HolderLookup
import net.minecraft.data.PackOutput
import net.minecraft.data.loot.LootTableProvider
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
import java.util.concurrent.CompletableFuture

class BurgeredLootTableProvider(arg: PackOutput, completableFuture: CompletableFuture<HolderLookup.Provider>) : LootTableProvider(
    arg,
    setOf(),
    listOf(
        SubProviderEntry(::BurgeredBlockLootTableProvider, LootContextParamSets.BLOCK)
    ), completableFuture
)