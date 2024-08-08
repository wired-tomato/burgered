package net.wiredtomato.burgered.init

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.level.block.state.BlockBehaviour
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.registry.RegistryDelegate
import net.wiredtomato.burgered.api.registry.registered
import net.wiredtomato.burgered.block.BurgerStackerBlock
import net.wiredtomato.burgered.block.GrillBlock

object BurgeredBlocks {
    val BLOCKS = DeferredRegister.create(Burgered.MOD_ID, Registries.BLOCK)

    val BURGER_STACKER by registering("burger_stacker") { BurgerStackerBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)) }
    val GRILL by registering("grill") { GrillBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE)) }

    fun <T : Block> registering(name: String, block: () -> T): RegistryDelegate<BurgeredBlocks, T> {
        return registered(BLOCKS.register(Burgered.modLoc(name), block))
    }
}
