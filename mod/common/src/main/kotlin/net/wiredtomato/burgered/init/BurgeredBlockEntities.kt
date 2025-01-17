package net.wiredtomato.burgered.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity
import net.wiredtomato.burgered.block.entity.GrillEntity
import net.wiredtomato.burgered.service.BlockEntityTypeFactoryImpl
import net.wiredtomato.burgered.service.RegistryServiceImpl

object BurgeredBlockEntities {
    val BURGER_STACKER by registering("burger_stacker") {
        BlockEntityTypeFactoryImpl.createBlockEntityType(::BurgerStackerEntity, setOf(BurgeredBlocks.BURGER_STACKER))
    }
    val GRILL by registering("grill") {
        BlockEntityTypeFactoryImpl.createBlockEntityType(::GrillEntity, setOf(BurgeredBlocks.GRILL))
    }

    fun <T : BlockEntity> registering(name: String, type: () -> BlockEntityType<T>): KSupplier<BlockEntityType<T>> {
        return RegistryServiceImpl.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, Burgered.modLoc(name), type)
    }
}