package net.wiredtomato.burgered.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity
import net.wiredtomato.burgered.init.BurgeredBlockEntities

class BurgerStackerBlock(settings: Properties) : BaseEntityBlock(settings) {
    private val shape = makeShape()
    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::BurgerStackerBlock)

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val entity = world.getBlockEntity(pos) ?: return InteractionResult.PASS
        if (entity !is BurgerStackerEntity) return InteractionResult.PASS
        val result: Pair<Component?, Boolean> = if (player.isCrouching) {
            entity.claimStack(player)
            null to true
        } else entity.addStack(player, player.mainHandItem)

        result.first?.let { player.displayClientMessage(it, false) }

        return if (result.second) InteractionResult.SUCCESS else InteractionResult.PASS
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BurgerStackerEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        world: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return createTickerHelper(type, BurgeredBlockEntities.BURGER_STACKER, BurgerStackerEntity)
    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext
    ): VoxelShape {
        return shape
    }

    fun makeShape(): VoxelShape {
        var shape = Shapes.empty()
        shape = Shapes.join(shape, Shapes.box(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), BooleanOp.OR)
        shape = Shapes.join(shape, Shapes.box(0.46875, 0.125, 0.46875, 0.53125, 1.0, 0.53125), BooleanOp.OR)

        return shape
    }
}