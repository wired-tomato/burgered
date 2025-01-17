package net.wiredtomato.burgered.fabric.service

import deplatformed.ServiceImpl
import net.fabricmc.fabric.api.`object`.builder.v1.block.entity.FabricBlockEntityTypeBuilder
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.wiredtomato.burgered.service.BlockEntityTypeFactory

@ServiceImpl(BlockEntityTypeFactory::class)
class FabricBlockEntityTypeFactory : BlockEntityTypeFactory {
    override fun <T : BlockEntity> createBlockEntityType(
        supplier: BlockEntityTypeFactory.BlockEntitySupplier<T>,
        validBlocks: Set<Block>
    ): BlockEntityType<T> {
        return FabricBlockEntityTypeBuilder.create<T>(supplier::create, *validBlocks.toTypedArray()).build()
    }
}