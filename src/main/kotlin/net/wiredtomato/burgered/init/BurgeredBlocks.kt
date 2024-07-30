package net.wiredtomato.burgered.init

import net.minecraft.block.AbstractBlock
import net.minecraft.block.Block
import net.minecraft.block.Blocks
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.block.BurgerStackerBlock

object BurgeredBlocks {
    val BURGER_STACKER = register("burger_stacker", BurgerStackerBlock(AbstractBlock.Settings.copy(Blocks.OAK_PLANKS)))

    fun <T : Block> register(name: String, block: T): T {
        return Registry.register(Registries.BLOCK, Burgered.id(name), block)
    }
}