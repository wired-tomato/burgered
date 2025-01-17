package net.wiredtomato.burgered.service

import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState

interface BlockEntityTypeFactory {
    fun <T : BlockEntity> createBlockEntityType(
        supplier: BlockEntitySupplier<T>,
        validBlocks: Set<Block> = setOf()
    ): BlockEntityType<T>

    fun interface BlockEntitySupplier<T : BlockEntity> {
        fun create(pos: BlockPos, state: BlockState): T
    }
}

val BlockEntityTypeFactoryImpl = Services.getService<BlockEntityTypeFactory>()
