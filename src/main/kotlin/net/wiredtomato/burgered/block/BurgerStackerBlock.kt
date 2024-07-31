package net.wiredtomato.burgered.block

import arrow.core.Option
import arrow.core.none
import com.mojang.serialization.MapCodec
import net.minecraft.block.BlockRenderType
import net.minecraft.block.BlockState
import net.minecraft.block.BlockWithEntity
import net.minecraft.block.ShapeContext
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.block.entity.BlockEntityType
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.text.Text
import net.minecraft.util.ActionResult
import net.minecraft.util.function.BooleanBiFunction
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.shape.VoxelShape
import net.minecraft.util.shape.VoxelShapes
import net.minecraft.world.BlockView
import net.minecraft.world.World
import net.wiredtomato.burgered.block.entity.BurgerStackerEntity
import net.wiredtomato.burgered.init.BurgeredBlockEntities

class BurgerStackerBlock(settings: Settings) : BlockWithEntity(settings) {
    private val shape = makeShape()
    override fun getCodec(): MapCodec<out BlockWithEntity> = createCodec(::BurgerStackerBlock)

    override fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        val entity = world.getBlockEntity(pos) ?: return ActionResult.PASS
        if (entity !is BurgerStackerEntity) return ActionResult.PASS
        val result: Option<Text> = if (player.isSneaking) {
            entity.claimStack(player)
            none()
        } else entity.addStack(player, player.mainHandStack)

        return if (result.isNone()) ActionResult.SUCCESS else ActionResult.PASS
    }

    override fun createBlockEntity(pos: BlockPos, state: BlockState): BlockEntity {
        return BurgerStackerEntity(pos, state)
    }

    override fun <T : BlockEntity> getTicker(
        world: World,
        state: BlockState,
        type: BlockEntityType<T>
    ): BlockEntityTicker<T>? {
        return checkType(type, BurgeredBlockEntities.BURGER_STACKER, BurgerStackerEntity)
    }

    override fun getRenderType(state: BlockState): BlockRenderType {
        return BlockRenderType.MODEL
    }

    override fun getOutlineShape(
        state: BlockState,
        world: BlockView,
        pos: BlockPos,
        context: ShapeContext
    ): VoxelShape {
        return shape
    }

    fun makeShape(): VoxelShape {
        var shape = VoxelShapes.empty()
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.0, 0.0, 0.0, 1.0, 0.125, 1.0), BooleanBiFunction.OR)
        shape = VoxelShapes.combine(shape, VoxelShapes.cuboid(0.46875, 0.125, 0.46875, 0.53125, 1.0, 0.53125), BooleanBiFunction.OR)

        return shape
    }
}