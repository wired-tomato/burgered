package net.wiredtomato.burgered.block.entity

import net.minecraft.core.BlockPos
import net.minecraft.core.HolderLookup
import net.minecraft.nbt.CompoundTag
import net.minecraft.network.chat.Component
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.game.ClientGamePacketListener
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import net.minecraft.world.level.block.Block
import net.minecraft.world.level.block.entity.BlockEntity
import net.minecraft.world.level.block.entity.BlockEntityTicker
import net.minecraft.world.level.block.state.BlockState
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.init.BurgeredBlockEntities
import net.wiredtomato.burgered.init.BurgeredDataComponents
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.item.BurgerItem
import net.wiredtomato.burgered.item.components.BurgerComponent
import kotlin.math.absoluteValue

class BurgerStackerEntity(
    pos: BlockPos,
    state: BlockState
) : BlockEntity(BurgeredBlockEntities.BURGER_STACKER, pos, state) {
    private var burger: ItemStack = ItemStack.EMPTY
    private var ticksSinceLastChange = 0

    fun addStack(player: Player, stack: ItemStack): Pair<Component?, Boolean> {
        val item = stack.item
        var result: Component? = null
        var interact = false
        if (item is BurgerIngredient) {
            ensureNonEmptyBurger()
            result = addIngredient(player, stack, item)
            interact = true
        } else if (item is BurgerItem) {
            ensureNonEmptyBurger()
            val ingredients = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT).ingredients()
            result = null
            ingredients.forEach { ingredient ->
                val oResult = addIngredient(player, ingredient.first, ingredient.second, false, updateSloppy = false)
                if (oResult != null) result = oResult
            }
            updateSloppiness(burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT))
            val component = burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
            val sloppiness = component.sloppiness()
            val otherSloppiness = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT).sloppiness()
            if (otherSloppiness > sloppiness) {
                BurgerComponent.setSloppiness(component, burger, otherSloppiness)
            }
            stack.consume(1, player)
            interact = true
        }

        return result to interact
    }

    fun claimStack(player: Player) {
        player.addItem(burger)
        burger = ItemStack.EMPTY
        setChanged()
    }

    fun renderStack() = burger.copy()

    override fun saveAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        if (!burger.isEmpty) {
            val stackNbt = burger.save(lookupProvider)
            nbt.put("burger", stackNbt)
        }
    }

    override fun loadAdditional(nbt: CompoundTag, lookupProvider: HolderLookup.Provider) {
        val stackNbt = nbt.getCompound("burger")
        burger = if (!stackNbt.isEmpty) {
            ItemStack.parseOptional(lookupProvider, stackNbt)
        } else ItemStack.EMPTY
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

    override fun setChanged() {
        level?.sendBlockUpdated(worldPosition, blockState, blockState, Block.UPDATE_CLIENTS)
        super.setChanged()
    }

    fun addIngredient(player: Player, stack: ItemStack, ingredient: BurgerIngredient, consume: Boolean = true, updateSloppy: Boolean = true): Component? {
        var burgerComponent = burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        val result = BurgerComponent.appendIngredient(burgerComponent, burger, stack.copy(), ingredient)
        burgerComponent = burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        if (result == null) {
            setChanged()
            if (updateSloppy) updateSloppiness(burgerComponent)
            if (consume) stack.consume(1, player)
        }

        if (updateSloppy) ticksSinceLastChange = 0
        return result
    }

    fun updateSloppiness(component: BurgerComponent) {
        if (ticksSinceLastChange <= 4) {
            val sloppiness = (component.sloppiness() + (0.02 *  (1 - ticksSinceLastChange).absoluteValue)).coerceAtMost(1.0)
            BurgerComponent.setSloppiness(component, burger, sloppiness)
        }

        ticksSinceLastChange = 0
    }

    fun ensureNonEmptyBurger() {
        if (burger.isEmpty) {
            burger = ItemStack(BurgeredItems.BURGER)
            burger.set(BurgeredDataComponents.BURGER, BurgerComponent())
        }
    }

    companion object : BlockEntityTicker<BurgerStackerEntity> {
        override fun tick(
            world: Level,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: BurgerStackerEntity
        ) {
            blockEntity.ticksSinceLastChange++
        }
    }
}