package net.wiredtomato.burgered.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.block.BurgerStackerBlock
import net.wiredtomato.burgered.block.GrillBlock
import net.wiredtomato.burgered.service.RegistryServiceImpl

object BurgeredBlocks {
    val BURGER_STACKER by registering("burger_stacker", ::BurgerStackerBlock) {
        BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)
    }

    val GRILL by registering("grill", ::GrillBlock) {
        BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)
    }

    fun <T : Block> registering(name: String, block: (BlockBehaviour.Properties) -> T, properties: KSupplier<BlockBehaviour.Properties>): KSupplier<T> {
        val id = Burgered.modLoc(name)
        return RegistryServiceImpl.register(BuiltInRegistries.BLOCK, id) {
            block(properties.get().setId(ResourceKey.create(Registries.BLOCK, id)))
        }
    }
}
