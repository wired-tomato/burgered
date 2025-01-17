package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.client.data.models.BlockModelGenerators
import net.minecraft.client.data.models.ItemModelGenerators
import net.minecraft.client.data.models.ModelProvider
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator
import net.minecraft.client.data.models.blockstates.Variant
import net.minecraft.client.data.models.blockstates.VariantProperties
import net.minecraft.client.data.models.model.ItemModelUtils
import net.minecraft.client.data.models.model.ModelLocationUtils
import net.minecraft.client.data.models.model.ModelTemplates
import net.minecraft.data.PackOutput
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.client.rendering.item.BurgerItemRenderer
import net.wiredtomato.burgered.init.BurgeredBlocks
import net.wiredtomato.burgered.init.BurgeredItems

class BurgeredModelProvider(
    output: PackOutput
) : ModelProvider(output, Burgered.MOD_ID) {
    override fun registerModels(block: BlockModelGenerators, item: ItemModelGenerators) {
        block.blockStateOutput.accept(
            BlockModelGenerators.createSimpleBlock(BurgeredBlocks.BURGER_STACKER, Burgered.modLoc("block/burger_stacker"))
        )
        block.registerSimpleItemModel(BurgeredBlocks.BURGER_STACKER, Burgered.modLoc("block/burger_stacker"))

        block.blockStateOutput.accept(
            MultiVariantGenerator.multiVariant(
                BurgeredBlocks.GRILL,
                Variant.variant()
                    .with(VariantProperties.MODEL, Burgered.modLoc("block/grill"))
            ).with(BlockModelGenerators.createHorizontalFacingDispatch())
        )
        block.registerSimpleItemModel(BurgeredBlocks.GRILL, Burgered.modLoc("block/grill"))

        val burgerModel = ItemModelUtils.specialModel(
            ModelLocationUtils.getModelLocation(BurgeredItems.BURGER),
            BurgerItemRenderer.Unbaked
        )
        item.itemModelOutput.accept(BurgeredItems.BURGER, burgerModel)
        item.declareCustomModelItem(BurgeredItems.TOP_BUN)
        item.declareCustomModelItem(BurgeredItems.BOTTOM_BUN)
        item.declareCustomModelItem(BurgeredItems.RAW_BEEF_PATTY)
        item.declareCustomModelItem(BurgeredItems.BEEF_PATTY)
        item.declareCustomModelItem(BurgeredItems.CHEESE_SLICE)
        item.declareCustomModelItem(BurgeredItems.LETTUCE)
        item.declareCustomModelItem(BurgeredItems.EDIBLE_BOOK)
        item.declareCustomModelItem(BurgeredItems.PICKLED_BEETS)
        item.generateFlatItem(BurgeredItems.BOOK_OF_BURGERS, ModelTemplates.FLAT_ITEM)
        item.generateFlatItem(BurgeredItems.ESTROGEN_WAFFLE, ModelTemplates.FLAT_ITEM)
    }
}
