package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.client.renderer.block.model.BlockModel.GuiLight
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.Item
import net.neoforged.neoforge.client.model.generators.ItemModelProvider
import net.neoforged.neoforge.client.model.generators.ModelFile
import net.neoforged.neoforge.common.data.ExistingFileHelper
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.util.id

class BurgeredItemModelProvider(
    output: PackOutput,
    existingFileHelper: ExistingFileHelper
) : ItemModelProvider(output, Burgered.MOD_ID, existingFileHelper) {
    override fun registerModels() {
        val DYNAMICLY_RENDERED = ModelFile.UncheckedModelFile(ResourceLocation.withDefaultNamespace("builtin/entity"))
        val TOP_BUN_MODEL = ModelFile.UncheckedModelFile(Burgered.modLoc("item/top_bun"))

        parented(BurgeredItems.BURGER, DYNAMICLY_RENDERED).guiLight(GuiLight.FRONT)
        parented(BurgeredItems.VANILLA_INGREDIENT, DYNAMICLY_RENDERED).guiLight(GuiLight.FRONT)
        parented(BurgeredItems.CUSTOM_BURGER_INGREDIENT, TOP_BUN_MODEL)

        basicItem(BurgeredItems.BOOK_OF_BURGERS)
        basicItem(BurgeredItems.ESTROGEN_WAFFLE)
    }

    fun parented(item: Item, parent: ModelFile) = getBuilder(item.id.toString()).parent(parent)
}
