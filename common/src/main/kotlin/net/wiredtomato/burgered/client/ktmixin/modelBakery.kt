package net.wiredtomato.burgered.client.ktmixin

import net.minecraft.client.color.block.BlockColors
import net.minecraft.client.renderer.block.model.BlockModel
import net.minecraft.client.resources.model.BlockStateModelLoader
import net.minecraft.client.resources.model.ModelBakery
import net.minecraft.client.resources.model.ModelResourceLocation
import net.minecraft.client.resources.model.UnbakedModel
import net.minecraft.resources.ResourceLocation
import net.minecraft.util.profiling.ProfilerFiller
import net.wiredtomato.burgered.Burgered
import java.util.function.BiConsumer

fun loadCustomModels(
    bakery: ModelBakery,
    blockColors: BlockColors,
    profiler: ProfilerFiller,
    blockModels: Map<ResourceLocation, BlockModel>,
    blockStates: Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>>,
    modelRegistry: BiConsumer<ModelResourceLocation, UnbakedModel>
) {
    blockModels.forEach { (resourceLocation, model) ->
        if (resourceLocation.path.startsWith("models/custom/")) {
            val path = resourceLocation.path.removePrefix("models/").removeSuffix(".json")
            val modelLocation = ModelResourceLocation(ResourceLocation.fromNamespaceAndPath(resourceLocation.namespace, path), "")
            modelRegistry.accept(modelLocation, model)
            Burgered.LOGGER.info("Registered $modelLocation")
        }
    }
}
