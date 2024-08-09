package net.wiredtomato.burgered.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.ContainerHelper
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.init.BurgeredRecipes
import net.wiredtomato.burgered.recipe.GrillingRecipe
import kotlin.math.roundToInt

class GrillEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(BurgeredBlockEntities.GRILL, pos, state) {
    private var cookTimes = mutableListOf(0, 0)
    private val inventory = NonNullList.withSize(2, ItemStack.EMPTY)

    fun onUse(
        state: BlockState,
        world: Level,
        pos: BlockPos,
        player: Player,
        hitResult: BlockHitResult
    ): InteractionResult {
        val stack = player.mainHandItem
        val hitPos = hitResult.location

        val center = pos.center
        return when (state.getValue(HorizontalDirectionalBlock.FACING)) {
            Direction.NORTH, Direction.SOUTH -> {
                if (hitPos.x > center.x) {
                    getOrGiveItem(world, player, stack, 0)
                } else getOrGiveItem(world, player, stack, 1)
            }
            Direction.EAST, Direction.WEST -> {
                if (hitPos.z < center.z) {
                    getOrGiveItem(world, player, stack, 0)
                } else getOrGiveItem(world, player, stack, 1)
            }

            else -> InteractionResult.PASS
        }
    }

    fun getOrGiveItem(world: Level, player: Player, stack: ItemStack, slot: Int): InteractionResult {
        val recipe = getRecipe(world, SingleRecipeInput(stack))
        val stack = player.mainHandItem
        val inventoryItem = inventory[slot].copy()
        val expandable = (stack.maxStackSize - stack.count).coerceAtMost(inventoryItem.count)
        if (!inventory[slot].isEmpty) {
            stack.count += expandable
            if (inventoryItem.count > expandable) {
                inventoryItem.count -= expandable
                player.addItem(inventoryItem)
            }
            inventory[slot] = ItemStack.EMPTY
            return InteractionResult.SUCCESS
        }

        if (recipe == null) {
            return InteractionResult.PASS
        } else {
            inventory[slot] = stack.copyWithCount(1)
            stack.consume(1, player)
        }

        setChanged()
        return InteractionResult.SUCCESS
    }

    fun renderStacks() = inventory.toList().map { it.copy() }

    override fun setChanged() {
        level?.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_CLIENTS)
        super.setChanged()
    }

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        nbt.putIntArray("cookTimes", cookTimes)
        ContainerHelper.saveAllItems(nbt, inventory, lookupProvider)
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        cookTimes = nbt.getIntArray("cookTimes").toMutableList()
        ContainerHelper.loadAllItems(nbt, inventory, lookupProvider)
    }

    override fun getUpdateTag(lookupProvider: HolderLookup.Provider): CompoundTag {
        val compound = CompoundTag()
        saveAdditional(compound, lookupProvider)
        return compound
    }

    override fun getUpdatePacket(): Packet<ClientGamePacketListener> {
        return ClientboundBlockEntityDataPacket.create(this) { entity, manager ->
            entity.getUpdateTag(manager)
        }
    }

    companion object : BlockEntityTicker<GrillEntity> {
        override fun tick(world: Level, blockPos: BlockPos, blockState: BlockState, grill: GrillEntity) = with(grill) {
            inventory.forEachIndexed { i, stack ->
                if (stack.isEmpty) return@forEachIndexed

                val recipeInput = SingleRecipeInput(stack)
                val recipeHolder = getRecipe(world, recipeInput) ?: return@forEachIndexed
                cookTimes[i]++
                val cookTime = cookTimes[i]
                val recipe = recipeHolder.value
                if (cookTime >= recipe.cookingTime) {
                    val result = recipe.assemble(recipeInput, world.registryAccess())
                    val transform = recipe.transform
                    inventory[i] = result
                    cookTimes[i] = 0

                    if (!transform.isEmpty) {
                        Block.popResource(world, blockPos, transform)
                    }

                    if (world is ServerLevel) {
                        val pos = blockPos.center
                        ExperienceOrb.award(world, pos, recipe.experience.roundToInt())
                    }
                }
            }
        }

        fun getRecipe(world: Level, input: SingleRecipeInput): RecipeHolder<GrillingRecipe>? {
            return world.recipeManager.getAllRecipesFor(BurgeredRecipes.GRILLING).find { it.value.matches(input, world) }
        }
    }
}