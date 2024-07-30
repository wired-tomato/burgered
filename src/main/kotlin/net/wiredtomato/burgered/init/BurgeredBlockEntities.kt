package net.wiredtomato.burgered.init

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity

object BurgeredBlockEntities {
    val BURGER_STACKER = register("burger_stacker", BlockEntityType.Builder.create(::BurgerStackerEntity, BurgeredBlocks.BURGER_STACKER).build())

    fun <T : BlockEntity> register(name: String, type: BlockEntityType<T>): BlockEntityType<T> {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, name, type)
    }
}