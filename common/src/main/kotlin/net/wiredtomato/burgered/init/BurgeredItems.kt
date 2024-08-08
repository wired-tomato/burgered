package net.wiredtomato.burgered.init

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.registry.RegistryDelegate
import net.wiredtomato.burgered.api.registry.registered
import net.wiredtomato.burgered.item.BurgerIngredientItem
import net.wiredtomato.burgered.item.BurgerIngredientItem.BurgerIngredientProperties
import net.wiredtomato.burgered.item.BurgerItem
import net.wiredtomato.burgered.item.VanillaItemBurgerIngredientItem

object BurgeredItems {
    val ITEMS = DeferredRegister.create(Burgered.MOD_ID, Registries.ITEM)

    val TOP_BUN by registering(
        "top_bun"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(1)
                .overSaturation(1.0)
                .modelHeight(2.0)
        )
    }

    val BOTTOM_BUN by registering(
        "bottom_bun"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(1)
                .overSaturation(1.0)
                .modelHeight(2.0)
        )
    }

    val RAW_BEEF_PATTY by registering(
        "raw_beef_patty"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(1)
                .overSaturation(2.0)
                .modelHeight(1.0)
                .statusEffect(MobEffectInstance(MobEffects.POISON, 200, 2), 0.25f)
        )
    }

    val BEEF_PATTY by registering(
        "beef_patty"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(4)
                .overSaturation(8.0)
                .modelHeight(1.0)
        )
    }

    val CHEESE_SLICE by registering(
        "cheese_slice"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(1)
                .overSaturation(0.25)
                .modelHeight(1.0)
        )
    }

    val LETTUCE by registering(
        "lettuce"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(1)
                .overSaturation(0.25)
                .modelHeight(0.0)
        )
    }

    val EDIBLE_BOOK by registering(
        "edible_book"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(7)
                .overSaturation(8.0)
                .modelHeight(4.0)
        )
    }

    val PICKLED_BEETS by registering(
        "pickled_beets"
    ) {
        BurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(2)
                .overSaturation(1.0)
                .modelHeight(0.0)
        )
    }

    val ESTROGEN_WAFFLE by registering("estrogen_waffle") { Item(Item.Properties()) }

    val CUSTOM_BURGER_INGREDIENT by registering(
        "custom_burger_ingredient"
    ) { Item(Item.Properties()) }

    val VANILLA_INGREDIENT by registering(
        "vanilla_ingredient"
    ) {
        VanillaItemBurgerIngredientItem(
            BurgerIngredientProperties()
                .saturation(2)
                .overSaturation(4.0)
                .modelHeight(1.0)
        )
    }

    val BURGER by registering(
        "burger"
    ) {
        BurgerItem(Item.Properties())
    }

    val BOOK_OF_BURGERS by registering(
        "book_of_burgers"
    ) { Item(Item.Properties()) }

    val BURGER_STACKER by registering("burger_stacker") { BlockItem(BurgeredBlocks.BURGER_STACKER, Item.Properties()) }
    val GRILL by registering("grill") { BlockItem(BurgeredBlocks.GRILL, Item.Properties()) }

    fun <T : Item> registering(name: String, itemSupplier: () -> T): RegistryDelegate<BurgeredItems, T> {
        return registered(ITEMS.register(Burgered.modLoc(name), itemSupplier))
    }
}
