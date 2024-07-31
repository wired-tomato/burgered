package net.wiredtomato.burgered.api.data.gen

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider
import net.minecraft.data.DataPackOutput
import net.minecraft.item.Item
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.data.burger.BurgerStackable
import java.util.concurrent.CompletableFuture
import java.util.function.BiConsumer

abstract class BurgerStackableProvider(
    dataOutput: FabricDataOutput,
    registryLookup: CompletableFuture<HolderLookup.Provider>,
    private val modId: String
) : FabricCodecDataProvider<BurgerStackable>(
    dataOutput,
    registryLookup,
    DataPackOutput.Type.DATA_PACK,
    "${Burgered.MOD_ID}/stackables",
    BurgerStackable.CODEC
) {
    override fun configure(provider: BiConsumer<Identifier, BurgerStackable>, lookup: HolderLookup.Provider) {
        configure(lookup) { stackable, id -> provider.accept(Identifier.of(modId, id.path), stackable) }
    }

    abstract fun configure(lookup: HolderLookup.Provider, builder: StackablesBuilder)

    override fun getName(): String = "Burger Stackables"

    fun interface StackablesBuilder {
        fun offer(stackable: BurgerStackable, id: Identifier)
        fun offer(stackable: BurgerStackable) = offer(stackable, stackable.item.id())

        fun Item.id() = Registries.ITEM.getId(this)
    }
}
