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
                ItemModel3d.ID to ItemModel3d.CODEC,
                Block.ID to Block.CODEC
            )
        }

        val CODEC: Codec<IngredientRenderSettings> = ResourceLocation.CODEC.dispatch({ it.id() }, {
            idToCodec.getOrDefault(it, ItemModel2d.CODEC)
        })

        val STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC)
    }

    open class Rendered(
        renderScale: Vector3d,
        offset: Vector3d,
        override val rendererId: ResourceLocation,
    ) : IngredientRenderSettings(renderScale, offset), WithCustomRenderer {
        override fun id(): ResourceLocation = RENDERED_ID

        companion object {
            val RENDERED_ID = modLoc("rendered")

            val CODEC: MapCodec<Rendered> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(Rendered::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(Rendered::offset),
                    ResourceLocation.CODEC.fieldOf("rendererId").forGetter(Rendered::rendererId)
                ).apply(it, ::Rendered)
            }
        }
    }

    open class ItemModel2d(
        renderScale: Vector3d,
        offset: Vector3d
    ) : IngredientRenderSettings(renderScale, offset) {
        override fun id(): ResourceLocation = ID

        companion object {
            val ID = modLoc("item2d")
            val CODEC: MapCodec<ItemModel2d> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(ItemModel2d::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(ItemModel2d::offset)
                ).apply(it, ::ItemModel2d)
            }
        }
    }

    open class ItemModel3d(
        renderScale: Vector3d,
        offset: Vector3d,
        override val modelHeight: Double
    ) : IngredientRenderSettings(renderScale, offset), WithModelHeight {
        override fun id(): ResourceLocation = ID

        companion object {
            val ID = modLoc("item3d")
            val CODEC: MapCodec<ItemModel3d> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(ItemModel3d::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(ItemModel3d::offset),
                    Codec.DOUBLE.fieldOf("modelHeight").forGetter(ItemModel3d::modelHeight)
                ).apply(it, ::ItemModel3d)
            }
        }
    }

    open class Block(
        renderScale: Vector3d,
        offset: Vector3d,
        override val modelHeight: Double
    ) : IngredientRenderSettings(renderScale, offset), WithModelHeight {
        override fun id(): ResourceLocation = ID

        companion object {
            val ID = modLoc("block")
            val CODEC: MapCodec<Block> = RecordCodecBuilder.mapCodec {
                it.group(
                    Vector3dCodec.fieldOf("renderScale").forGetter(Block::renderScale),
                    Vector3dCodec.fieldOf("offset").forGetter(Block::offset),
                    Codec.DOUBLE.fieldOf("modelHeight").forGetter(Block::modelHeight)
                ).apply(it, ::Block)
            }
        }
    }
}

interface WithModelHeight {
    val modelHeight: Double
}

interface WithCustomRenderer {
    val rendererId: ResourceLocation
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
