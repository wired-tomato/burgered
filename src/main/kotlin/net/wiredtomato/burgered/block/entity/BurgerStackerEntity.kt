package net.wiredtomato.burgered.block.entity

import arrow.core.*
import net.minecraft.block.Block
import net.minecraft.block.BlockState
import net.minecraft.block.entity.BlockEntity
import net.minecraft.block.entity.BlockEntityTicker
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NbtCompound
import net.minecraft.network.listener.ClientPlayPacketListener
import net.minecraft.network.packet.Packet
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket
import net.minecraft.registry.HolderLookup
import net.minecraft.text.Text
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
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

    fun addStack(player: PlayerEntity, stack: ItemStack): Option<Text> {
        var result: Option<Text> = Text.empty().some()
        val item = stack.item
        if (item is BurgerIngredient) {
            ensureNonEmptyBurger()
            result = addIngredient(player, stack, item)
        } else if (item is BurgerItem) {
            ensureNonEmptyBurger()
            val ingredients = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT).ingredients()
            ingredients.forEach { ingredient -> addIngredient(player, stack, ingredient, false, updateSloppy = false) }
            updateSloppiness(burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT))
            result = none()
            val component = burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
            val sloppiness = component.sloppiness()
            val otherSloppiness = stack.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT).sloppiness()
            if (otherSloppiness > sloppiness) {
                BurgerComponent.setSloppiness(component, burger, otherSloppiness)
            }
            stack.consume(1, player)
        }

        return result
    }

    fun claimStack(player: PlayerEntity) {
        player.giveItemStack(burger)
        burger = ItemStack.EMPTY
        markDirty()
    }

    fun renderStack() = burger.copy()

    override fun writeNbt(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        if (!burger.isEmpty) {
            val stackNbt = burger.encode(lookupProvider)
            nbt.put("burger", stackNbt)
        }
    }

    override fun method_11014(nbt: NbtCompound, lookupProvider: HolderLookup.Provider) {
        val stackNbt = nbt.getCompound("burger")
        burger = if (!stackNbt.isEmpty) {
            ItemStack.fromNbt(lookupProvider, stackNbt)
        } else ItemStack.EMPTY
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

    override fun markDirty() {
        world?.updateListeners(pos, cachedState, cachedState, Block.NOTIFY_LISTENERS)
        super.markDirty()
    }

    fun addIngredient(player: PlayerEntity, stack: ItemStack, ingredient: BurgerIngredient, consume: Boolean = true, updateSloppy: Boolean = true): Option<Text> {
        var burgerComponent = burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        val result = BurgerComponent.appendIngredient(burgerComponent, burger, ingredient)
        burgerComponent = burger.getOrDefault(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        result.onNone {
            markDirty()
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
            world: World,
            blockPos: BlockPos,
            blockState: BlockState,
            blockEntity: BurgerStackerEntity
        ) {
            blockEntity.ticksSinceLastChange++
        }
    }
}