package net.wiredtomato.burgered.block.entity

import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.HorizontalFacingBlock
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.inventory.Inventories
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.recipe.RecipeHolder
import net.minecraft.recipe.SingleRecipeInput
import net.minecraft.registry.HolderLookup
import net.minecraft.util.ActionResult
import net.minecraft.util.Hand
import net.minecraft.util.collection.DefaultedList
import net.minecraft.util.hit.BlockHitResult
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.Direction
import net.minecraft.world.World
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.init.BurgeredRecipes
import net.wiredtomato.burgered.recipe.GrillingRecipe

class GrillEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(BurgeredBlockEntities.GRILL, pos, state) {
    private var cookTimes = mutableListOf(0, 0)
    private val inventory = DefaultedList.ofSize(2, ItemStack.EMPTY)

    fun onUse(
        state: BlockState,
        world: World,
        pos: BlockPos,
        player: PlayerEntity,
        hitResult: BlockHitResult
    ): ActionResult {
        val stack = player.getStackInHand(Hand.MAIN_HAND)

        val hitPos = hitResult.pos
        val northSouth = when (state.get(HorizontalFacingBlock.FACING)) {
            Direction.NORTH, Direction.SOUTH -> true
            else -> false
        }

        val center = pos.ofCenter()
        return if (northSouth) {
            if (hitPos.x > center.x) {
                getOrGiveItem(world, player, stack, 0)
            } else getOrGiveItem(world, player, stack, 1)
        } else {
            if (hitPos.z < center.z) {
                getOrGiveItem(world, player, stack, 0)
            } else getOrGiveItem(world, player, stack, 1)
        }
    }

    fun getOrGiveItem(world: World, player: PlayerEntity, stack: ItemStack, slot: Int): ActionResult {
        val recipe = getRecipe(world, SingleRecipeInput(stack))
        if (!inventory[slot].isEmpty) {
            player.giveItemStack(inventory[slot])
            inventory[slot] = ItemStack.EMPTY
            Burgered.LOGGER.info("gave")
            return ActionResult.SUCCESS
        }

        if (recipe == null) {
            return ActionResult.PASS
        } else {
            inventory[slot] = stack.copyWithCount(1)
            stack.consume(1, player)
        }

        markDirty()
        return ActionResult.SUCCESS
    }

    fun renderStacks() = inventory.toList().map { it.copy() }

    override fun markDirty() {
        world?.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_LISTENERS)
        super.markDirty()
    }

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        nbt.putIntArray("cookTimes", cookTimes)
        Inventories.writeNbt(nbt, inventory, lookupProvider)
    }

    override fun method_11014(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        cookTimes = nbt.getIntArray("cookTimes").toMutableList()
        Inventories.readNbt(nbt, inventory, lookupProvider)
    }

    override fun toSyncedNbt(lookupProvider: HolderLookup.Provider): NbtCompound {
        val compound = NbtCompound()
        writeNbt(compound, lookupProvider)
        return compound
    }

    override fun toUpdatePacket(): Packet<ClientPlayPacketListener> {
        return BlockEntityUpdateS2CPacket.create(this) { entity, manager ->
            entity.toSyncedNbt(manager)
        }
    }

    companion object : BlockEntityTicker<GrillEntity> {
        override fun tick(world: World, blockPos: BlockPos, blockState: BlockState, grill: GrillEntity) = with(grill) {
            inventory.forEachIndexed { i, stack ->
                if (stack.isEmpty) return@forEachIndexed

                val recipeInput = SingleRecipeInput(stack)
                val recipeHolder = getRecipe(world, recipeInput) ?: return@forEachIndexed
                cookTimes[i]++
                val cookTime = cookTimes[i]
                val recipe = recipeHolder.value
                if (cookTime >= recipe.cookTime) {
                    val result = recipe.craft(recipeInput, world.registryManager)
                    inventory[i] = result
                    cookTimes[i] = 0
                }
            }
        }

        fun getRecipe(world: World, input: SingleRecipeInput): RecipeHolder<GrillingRecipe>? {
            return world.recipeManager.listAllOfType(BurgeredRecipes.GRILLING).find { it.value.matches(input, world) }
        }
    }
}