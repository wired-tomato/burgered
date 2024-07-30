package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Model
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.init.BurgeredBlocks
import net.wiredtomato.burgered.init.BurgeredItems
import java.util.*

class BurgeredModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) = with(blockStateModelGenerator) {
        registerSimpleState(BurgeredBlocks.BURGER_STACKER)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) = with(itemModelGenerator) {
        this.register(BurgeredItems.BURGER, DYNAMIC_MODEL)
    }

    companion object {
        val DYNAMIC_MODEL = Model(Optional.of(Identifier.ofDefault("builtin/entity")), Optional.empty())
    }
}
