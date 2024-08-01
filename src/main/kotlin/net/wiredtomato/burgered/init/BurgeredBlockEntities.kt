package net.wiredtomato.burgered.init

import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity
import net.wiredtomato.burgered.block.entity.GrillEntity

object BurgeredBlockEntities {
    val BURGER_STACKER = register("burger_stacker", BlockEntityType.Builder.create(::BurgerStackerEntity, BurgeredBlocks.BURGER_STACKER).build())
    val GRILL = register("grill", BlockEntityType.Builder.create(::GrillEntity, BurgeredBlocks.GRILL).build())

    fun <T : BlockEntity> register(name: String, type: BlockEntityType<T>): BlockEntityType<T> {
        return Registry.register(Registries.BLOCK_ENTITY_TYPE, name, type)
    }
}