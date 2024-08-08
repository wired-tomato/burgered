package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.data.PackOutput
import net.neoforged.neoforge.client.model.generators.BlockStateProvider
import net.neoforged.neoforge.client.model.generators.ModelFile.UncheckedModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredBlocks

class BurgeredBlockStateProvider(
    output: PackOutput,
    exFileHelper: ExistingFileHelper
) : BlockStateProvider(output, Burgered.MOD_ID, exFileHelper) {
    override fun registerStatesAndModels() {
        val burgerStackerModel = UncheckedModelFile(Burgered.modLoc("block/burger_stacker"))
        val grillModel = UncheckedModelFile(Burgered.modLoc("block/grill"))
        simpleBlock(BurgeredBlocks.BURGER_STACKER, burgerStackerModel)
        horizontalBlock(BurgeredBlocks.GRILL, grillModel)

        simpleBlockItem(BurgeredBlocks.BURGER_STACKER, burgerStackerModel)
        simpleBlockItem(BurgeredBlocks.GRILL, grillModel)
    }
}
