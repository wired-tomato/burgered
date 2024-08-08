package net.wiredtomato.burgered.api.rendering

import net.minecraft.resources.ResourceLocation
import org.joml.Vector3d

sealed class IngredientRenderSettings(val renderScale: Vector3d, val offset: Vector3d) {
    open class ItemModel3d(renderScale: Vector3d, offset: Vector3d, override val modelHeight: Double) : IngredientRenderSettings(renderScale, offset), WithModelHeight {
        class CustomModeled(
            renderScale: Vector3d,
            offset: Vector3d,
            modelHeight: Double,
            override val customModelId: ModelId
        ) : ItemModel3d(renderScale, offset, modelHeight), WithCustomModel
    }

    open class ItemModel2d(renderScale: Vector3d, offset: Vector3d) : IngredientRenderSettings(renderScale, offset) {
        class CustomModeled(
            renderScale: Vector3d,
            offset: Vector3d,
            override val customModelId: ModelId
        ) : ItemModel2d(renderScale, offset), WithCustomModel
    }

    open class Block(renderScale: Vector3d, offset: Vector3d, override val modelHeight: Double) : IngredientRenderSettings(renderScale, offset), WithModelHeight {
        class CustomModeled(
            renderScale: Vector3d,
            offset: Vector3d,
            modelHeight: Double,
            override val customModelId: ModelId
        ) : Block(renderScale, offset, modelHeight), WithCustomModel
    }
}

interface WithModelHeight {
    val modelHeight: Double
}

interface WithCustomModel {
    val customModelId: ModelId
}

data class ModelId(val model: ResourceLocation, val variant: String)
