package net.wiredtomato.burgered.neoforge.service

import deplatformed.ServiceImpl
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.wiredtomato.burgered.service.BlockEntityTypeFactory

@ServiceImpl(BlockEntityTypeFactory::class)
class NeoForgeBlockEntityTypeFactory : BlockEntityTypeFactory {
    override fun <T : BlockEntity> createBlockEntityType(
        supplier: BlockEntityTypeFactory.BlockEntitySupplier<T>,
        validBlocks: Set<Block>
    ): BlockEntityType<T> {
        return BlockEntityType(supplier::create, validBlocks)
    }
}