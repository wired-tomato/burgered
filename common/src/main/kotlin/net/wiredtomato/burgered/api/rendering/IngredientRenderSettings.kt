package net.wiredtomato.burgered.api.rendering

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.network.codec.ByteBufCodecs
import net.minecraft.resources.ResourceLocation
import net.wiredtomato.burgered.Burgered.modLoc
import org.joml.Vector3d

sealed class IngredientRenderSettings(val renderScale: Vector3d, val offset: Vector3d) {
    abstract fun id(): ResourceLocation

    companion object {
        private val idToCodec by lazy {
            mutableMapOf<ResourceLocation, MapCodec<out IngredientRenderSettings>>(
                ItemModel2d.ID to ItemModel2d.CODEC,
                ItemModel2d.MODELED_ID to ItemModel2d.MODELED_CODEC,
                ItemModel3d.ID to ItemModel3d.CODEC,
                ItemModel3d.MODELED_ID to ItemModel3d.MODELED_CODEC,
                Block.ID to Block.CODEC,
                Block.MODELED_ID to Block.MODELED_CODEC
            )
        }

        val CODEC: Codec<IngredientRenderSettings> = ResourceLocation.CODEC.dispatch({ it.id() }, {
            idToCodec.getOrDefault(it, ItemModel2d.CODEC)
        })

        val STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC)
    }

    open class ItemModel2d(
        renderScale: Vector3d,
        offset: Vector3d
    ) : IngredientRenderSettings(renderScale, offset) {
        override fun id(): ResourceLocation = ID

        class CustomModeled(
            renderScale: Vector3d,
            offset: Vector3d,
            override val customModelId: ModelId
        ) : ItemModel2d(renderScale, offset), WithCustomModel {
            override fun id(): ResourceLocation = MODELED_ID
        }

        companion object {
            val ID = modLoc("item2d")
            val CODEC: MapCodec<ItemModel2d> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(ItemModel2d::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(ItemModel2d::offset)
                ).apply(it, ::ItemModel2d)
            }

            val MODELED_ID = modLoc("modeled_item2d")
            val MODELED_CODEC: MapCodec<CustomModeled> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(CustomModeled::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(CustomModeled::offset),
                    ModelId.CODEC.fieldOf("customModelId").forGetter(CustomModeled::customModelId)
                ).apply(it, ::CustomModeled)
            }
        }
    }

    open class ItemModel3d(
        renderScale: Vector3d,
        offset: Vector3d,
        override val modelHeight: Double
    ) : IngredientRenderSettings(renderScale, offset), WithModelHeight {
        override fun id(): ResourceLocation = ID

        class CustomModeled(
            renderScale: Vector3d,
            offset: Vector3d,
            modelHeight: Double,
            override val customModelId: ModelId
        ) : ItemModel3d(renderScale, offset, modelHeight), WithCustomModel {
            override fun id(): ResourceLocation = MODELED_ID
        }

        companion object {
            val ID = modLoc("item3d")
            val CODEC: MapCodec<ItemModel3d> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(ItemModel3d::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(ItemModel3d::offset),
                    Codec.DOUBLE.fieldOf("modelHeight").forGetter(ItemModel3d::modelHeight)
                ).apply(it, ::ItemModel3d)
            }

            val MODELED_ID = modLoc("modeled_item3d")

            @Suppress("duplicates")
            val MODELED_CODEC: MapCodec<CustomModeled> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(CustomModeled::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(CustomModeled::offset),
                    Codec.DOUBLE.fieldOf("modelHeight").forGetter(CustomModeled::modelHeight),
                    ModelId.CODEC.fieldOf("customModelId").forGetter(CustomModeled::customModelId)
                ).apply(it, ::CustomModeled)
            }
        }
    }

    open class Block(
        renderScale: Vector3d,
        offset: Vector3d,
        override val modelHeight: Double
    ) : IngredientRenderSettings(renderScale, offset), WithModelHeight {
        override fun id(): ResourceLocation = ID

        class CustomModeled(
            renderScale: Vector3d,
            offset: Vector3d,
            modelHeight: Double,
            override val customModelId: ModelId
        ) : Block(renderScale, offset, modelHeight), WithCustomModel {
            override fun id(): ResourceLocation = MODELED_ID
        }

        companion object {
            val ID = modLoc("block")
            val CODEC: MapCodec<Block> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(Block::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(Block::offset),
                    Codec.DOUBLE.fieldOf("modelHeight").forGetter(Block::modelHeight)
                ).apply(it, ::Block)
            }

            val MODELED_ID = modLoc("modeled_block")

            @Suppress("duplicates")
            val MODELED_CODEC: MapCodec<CustomModeled> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(CustomModeled::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(CustomModeled::offset),
                    Codec.DOUBLE.fieldOf("modelHeight").forGetter(CustomModeled::modelHeight),
                    ModelId.CODEC.fieldOf("customModelId").forGetter(CustomModeled::customModelId)
                ).apply(it, ::CustomModeled)
            }
        }
    }
}

interface WithModelHeight {
    val modelHeight: Double
}

interface WithCustomModel {
    val customModelId: ModelId
}

data class ModelId(val model: ResourceLocation, val variant: String) {
    companion object {
        val CODEC: Codec<ModelId> = RecordCodecBuilder.create {
            it.group(
                ResourceLocation.CODEC.fieldOf("model").forGetter(ModelId::model),
                Codec.STRING.fieldOf("variant").forGetter(ModelId::variant)
            ).apply(it, ::ModelId)
        }
    }
}
