package net.wiredtomato.burgered.block

import com.mojang.serialization.MapCodec
import net.minecraft.block.*
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemPlacementContext
import net.minecraft.state.StateManager
import net.minecraft.util.ActionResult
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.wiredtomato.burgered.block.entity.GrillEntity
import net.wiredtomato.burgered.init.BurgeredBlockEntities


class GrillBlock(settings: Settings) : BlockWithEntity(settings) {
    private val shape = makeShape()
    private val rotatedShape = makeRotatedShape()

    init {
        defaultState = stateManager.defaultState.with(HorizontalFacingBlock.FACING, Direction.NORTH)
    }

    override fun getCodec(): MapCodec<out BlockWithEntity> = createCodec(::BurgerStackerBlock)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        val entity = world.getBlockEntity(pos)
        if (entity !is GrillEntity) return ActionResult.PASS

        return entity.onUse(state, world, pos, player, hitResult)
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return GrillEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return checkType(type, BurgeredBlockEntities.GRILL, GrillEntity)
    }

    override fun getPlacementState(ctx: ItemPlacementContext): BlockState {
        return defaultState.with(HorizontalFacingBlock.FACING, ctx.playerFacing.opposite)
    }

    override fun appendProperties(builder: StateManager.Builder<Block, BlockState>) {
        builder.add(HorizontalFacingBlock.FACING)
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return when (state.get(HorizontalFacingBlock.FACING)) {
            Direction.NORTH, Direction.SOUTH -> shape
            else -> rotatedShape
        }
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    fun makeShape(): VoxelShape {
        var shape = VoxelShapes.empty()
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0, 0.5, 0.1875, 1.0, 0.75, 0.8125), BooleanBiFunction.OR)

        return shape
    }

    fun makeRotatedShape(): VoxelShape {
        var shape = VoxelShapes.empty()
        shape =
            VoxelShapes.combine(shape, VoxelShapes.cuboid(0.1875, 0.5, 0.0, 0.8125, 0.75, 1.0), BooleanBiFunction.OR)

        return shape
    }
}
