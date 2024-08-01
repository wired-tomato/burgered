package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider
import net.minecraft.data.client.ItemModelGenerator
import net.minecraft.data.client.model.BlockStateModelGenerator
import net.minecraft.data.client.model.Model
import net.minecraft.data.client.model.Models
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredBlocks
import net.wiredtomato.burgered.init.BurgeredItems
import java.util.*

class BurgeredModelProvider(output: FabricDataOutput) : FabricModelProvider(output) {
    override fun generateBlockStateModels(blockStateModelGenerator: BlockStateModelGenerator) = with(blockStateModelGenerator) {
        registerSimpleState(BurgeredBlocks.BURGER_STACKER)
        registerNorthDefaultHorizontalRotation(BurgeredBlocks.GRILL)
    }

    override fun generateItemModels(itemModelGenerator: ItemModelGenerator) = with(itemModelGenerator) {
        this.register(BurgeredItems.BOOK_OF_BURGERS, Models.SINGLE_LAYER_ITEM)
        this.register(BurgeredItems.ESTROGEN_WAFFLE, Models.SINGLE_LAYER_ITEM)
        this.register(BurgeredItems.BURGER, DYNAMIC_MODEL)
        this.register(BurgeredItems.VANILLA_INGREDIENT, DYNAMIC_MODEL)
        this.register(BurgeredItems.CUSTOM_BURGER_INGREDIENT, TOP_BUN_MODEL)
    }

    companion object {
        val DYNAMIC_MODEL = Model(Optional.of(Identifier.ofDefault("builtin/entity")), Optional.empty())
        val TOP_BUN_MODEL = Model(Optional.of(Burgered.id("item/top_bun")), Optional.empty())
    }
}
