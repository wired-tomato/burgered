package net.wiredtomato.burgered.block

import com.mojang.serialization.MapCodec
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.level.BlockGetter
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.BaseEntityBlock
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.RenderShape
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.entity.BlockEntityType
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.level.block.state.StateDefinition
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.shapes.BooleanOp
import net.minecraft.world.phys.shapes.CollisionContext
import net.minecraft.world.phys.shapes.Shapes
import net.minecraft.world.phys.shapes.VoxelShape
import net.wiredtomato.burgered.block.entity.GrillEntity
import net.wiredtomato.burgered.init.BurgeredBlockEntities


class GrillBlock(properties: Properties) : BaseEntityBlock(properties) {
    private val shape = makeShape()
    private val rotatedShape = makeRotatedShape()

    init {
        registerDefaultState(stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH))
    }

    override fun codec(): MapCodec<out BaseEntityBlock> = simpleCodec(::GrillBlock)

    override fun useWithoutItem(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val entity = world.getBlockEntity(pos)
        if (entity !is GrillEntity) return InteractionResult.PASS

        return entity.onUse(state, world, pos, player, hitResult)
    }

    override fun newBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return GrillEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        world: Level,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return createTickerHelper(type, BurgeredBlockEntities.GRILL, GrillEntity)
    }

    override fun getStateForPlacement(ctx: BlockPlaceContext): BlockState {
        return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, ctx.clickedFace.opposite)
    }

    override fun createBlockStateDefinition(builder: StateDefinition.Builder<Block, BlockState>) {
        builder.add(HorizontalDirectionalBlock.FACING)
    }

    override fun getShape(
        blockState: BlockState,
        blockGetter: BlockGetter,
        blockPos: BlockPos,
        collisionContext: CollisionContext
    ): VoxelShape {
        return when (blockState.getValue(HorizontalDirectionalBlock.FACING)) {
            Direction.NORTH, Direction.SOUTH -> shape
            else -> rotatedShape
        }
    }

    override fun getRenderShape(blockState: BlockState): RenderShape {
        return RenderShape.MODEL
    }

    fun makeShape(): VoxelShape {
        var shape = Shapes.empty()
        shape = Shapes.join(shape, Shapes.box(0.0, 0.5, 0.1875, 1.0, 0.75, 0.8125), BooleanOp.OR)

        return shape
    }

    fun makeRotatedShape(): VoxelShape {
        var shape = Shapes.empty()
        shape =
            Shapes.join(shape, Shapes.box(0.1875, 0.5, 0.0, 0.8125, 0.75, 1.0), BooleanOp.OR)

        return shape
    }
}
