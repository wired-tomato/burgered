package net.wiredtomato.burgered.init

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityType
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.registry.RegistryDelegate
import net.wiredtomato.burgered.api.registry.registered
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity
import net.wiredtomato.burgered.block.entity.GrillEntity

object BurgeredBlockEntities {
    val BLOCK_ENTITIES = DeferredRegister.create(Burgered.MOD_ID, Registries.BLOCK_ENTITY_TYPE)

    val BURGER_STACKER by registering("burger_stacker") {
        BlockEntityType.Builder.of(::BurgerStackerEntity, BurgeredBlocks.BURGER_STACKER).build(null)
    }
    val GRILL by registering("grill") { BlockEntityType.Builder.of(::GrillEntity, BurgeredBlocks.GRILL).build(null) }

    fun <T : BlockEntity> registering(name: String, type: () -> BlockEntityType<T>): RegistryDelegate<BurgeredBlockEntities, BlockEntityType<T>> {
        return registered(BLOCK_ENTITIES.register(Burgered.modLoc(name), type))
    }
}
