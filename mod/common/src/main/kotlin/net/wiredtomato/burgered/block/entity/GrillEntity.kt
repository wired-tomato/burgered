package net.wiredtomato.burgered.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.HolderLookup
import net.minecraft.core.NonNullList
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.server.level.ServerLevel
import net.minecraft.sounds.SoundEvents
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.entity.ExperienceOrb
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.crafting.RecipeHolder
import net.minecraft.world.item.crafting.SingleRecipeInput
import net.minecraft.world.level.GameRules
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.HorizontalDirectionalBlock
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredient
import net.wiredtomato.burgered.api.burger.ingredient.IngredientQuality
import net.wiredtomato.burgered.api.burger.ingredient.IngredientQuality.Companion.defaultQuality
import net.wiredtomato.burgered.api.burger.ingredient.IngredientQuality.Companion.withQuality
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.init.BurgeredRecipes
import net.wiredtomato.burgered.recipe.GrillingRecipe
import net.wiredtomato.burgered.api.util.load
import net.wiredtomato.burgered.api.util.save
import java.util.Optional
import kotlin.jvm.optionals.getOrNull
import kotlin.math.roundToInt

class GrillEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(BurgeredBlockEntities.GRILL, pos, state) {
    private var cookTimes = mutableListOf(0, 0)
    private var skillCheckTimes = mutableListOf(SKILL_CHECK_NAN, SKILL_CHECK_NAN)
    private var qualities = mutableListOf(IngredientQuality.EXCELLENT, IngredientQuality.EXCELLENT)
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
                    slotInteraction(world, player, stack, 0)
                } else slotInteraction(world, player, stack, 1)
            }

            Direction.EAST, Direction.WEST -> {
                if (hitPos.z < center.z) {
                    slotInteraction(world, player, stack, 0)
                } else slotInteraction(world, player, stack, 1)
            }

            else -> InteractionResult.PASS
        }
    }

    fun slotInteraction(world: Level, player: Player, stack: ItemStack, slot: Int): InteractionResult {
        val recipe = getRecipe(world, SingleRecipeInput(stack)).getOrNull()
        if (isDuringSkillCheck(slot)) {
            if (shouldIncreaseQuality(slot)) {
                qualities[slot] = qualities[slot].nextUp()
                world.playSound(
                    null,
                    blockPos,
                    SoundEvents.EXPERIENCE_ORB_PICKUP,
                    SoundSource.BLOCKS,
                    0.2f,
                    3f
                )
            }

            resetSkillCheckTime(slot)
            return InteractionResult.SUCCESS_SERVER
        }

        if (!inventory[slot].isEmpty) {
            world.spawnItems(slot, inventory[slot])
            inventory[slot] = ItemStack.EMPTY
            cookTimes[slot] = 0
            skillCheckTimes[slot] = SKILL_CHECK_NAN
            qualities[slot] = IngredientQuality.NORMAL
            setChanged()
            return InteractionResult.SUCCESS_SERVER
        }

        if (recipe == null) {
            return InteractionResult.PASS
        } else {
            inventory[slot] = stack.copyWithCount(1)
            qualities[slot] = stack.defaultQuality()
            resetSkillCheckTime(slot)
            stack.consume(1, player)
        }

        setChanged()
        return InteractionResult.SUCCESS
    }

    fun getItemPos(slot: Int): Vec3 {
        var pos = blockPos.center.add(0.0, 0.4, 0.0)
        val direction = blockState.getValue(HorizontalDirectionalBlock.FACING)
        when (direction) {
            Direction.NORTH, Direction.SOUTH -> pos = pos.add(-0.25 * ((slot * 2) - 1), 0.0, 0.0)
            Direction.EAST, Direction.WEST -> pos = pos.add(0.0, 0.0, 0.25 * ((slot * 2) - 1))
            else -> {}
        }

        return pos
    }

    fun resetSkillCheckTime(slot: Int) {
        skillCheckTimes[slot] = 40
    }

    fun shouldIncreaseQuality(slot: Int): Boolean {
        val skillCheckTime = skillCheckTimes[slot]
        return skillCheckTime in (-20..0)
    }

    fun isDuringSkillCheck(slot: Int): Boolean {
        val skillCheckTime = skillCheckTimes[slot]
        return skillCheckTime in -40..0
    }

    fun renderStacks() = inventory.toList().map { it.copy() }

    override fun setChanged() {
        level?.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_CLIENTS)
        super.setChanged()
    }

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        nbt.putIntArray("cookTimes", cookTimes)
        nbt.putIntArray("ttSkillCheck", skillCheckTimes)
        inventory.save(nbt)
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        cookTimes = nbt.getIntArray("cookTimes").toMutableList()
        skillCheckTimes = nbt.getIntArray("ttSkillCheck").toMutableList()
        if (skillCheckTimes.isEmpty()) {
            skillCheckTimes = mutableListOf(SKILL_CHECK_NAN, SKILL_CHECK_NAN)
        }
        inventory.load(nbt)
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

    fun Level.spawnItems(slot: Int, stack: ItemStack) {
        val pos = getItemPos(slot)
        val itemEntity = ItemEntity(this, pos.x, pos.y, pos.z, stack)
        itemEntity.setDeltaMovement(0.0, 0.15, 0.0)
        itemEntity.setDefaultPickUpDelay()
        this.addFreshEntity(itemEntity)
    }

    companion object : BlockEntityTicker<GrillEntity> {
        private const val SKILL_CHECK_NAN = Int.MAX_VALUE

        override fun tick(world: Level, blockPos: BlockPos, blockState: BlockState, grill: GrillEntity) = with(grill) {
            inventory.forEachIndexed { i, stack ->
                if (stack.isEmpty) return@forEachIndexed

                val recipeInput = SingleRecipeInput(stack)
                val recipeHolder: RecipeHolder<GrillingRecipe> = getRecipe(world, recipeInput).getOrNull() ?: return@forEachIndexed

                skillCheckTimes[i]--
                val skillCheckTime = skillCheckTimes[i]
                if (isDuringSkillCheck(i) && world is ServerLevel) {
                    val particlePos = getItemPos(i).add(0.0, 0.2, 0.0)
                    world.sendParticles(
                        ParticleTypes.FLAME,
                        particlePos.x,
                        particlePos.y,
                        particlePos.z,
                        2,
                        0.0,
                        0.0,
                        0.0,
                        0.0
                    )
                }

                if (skillCheckTime < -40) {
                    qualities[i] = qualities[i].nextDown()
                    resetSkillCheckTime(i)
                    world.playSound(
                        null,
                        blockPos,
                        SoundEvents.BLAZE_SHOOT,
                        SoundSource.BLOCKS,
                        0.2f,
                        0f
                    )
                }

                cookTimes[i]++
                val cookTime = cookTimes[i]
                val recipe: GrillingRecipe = recipeHolder.value
                if (cookTime >= recipe.cookingTime()) {
                    val result = recipe.assemble(recipeInput, world.registryAccess())
                    if (result.item is BurgerIngredient) result.withQuality(qualities[i])
                    val transform = recipe.transform.copy()
                    inventory[i] = result
                    cookTimes[i] = 0

                    if (!transform.isEmpty && (world.server?.gameRules?.getBoolean(GameRules.RULE_DOBLOCKDROPS) == true)) {
                        world.spawnItems(i, transform)
                    }

                    if (world is ServerLevel) {
                        ExperienceOrb.award(world, getItemPos(i), (recipe.experience() * qualities[i].multiplier).roundToInt())
                    }
                }
            }
        }

        fun getRecipe(world: Level, input: SingleRecipeInput): Optional<RecipeHolder<GrillingRecipe>> {
            return world.server?.recipeManager?.getRecipeFor(BurgeredRecipes.GRILLING, input, world) ?: Optional.empty()
        }
    }
}
