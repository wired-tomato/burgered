package net.wiredtomato.burgered.init

import net.minecraft.entity.effect.StatusEffectInstance
import net.minecraft.entity.effect.StatusEffects
import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.item.BurgerIngredientItem
import net.wiredtomato.burgered.item.BurgerIngredientItem.BurgerIngredientSettings
import net.wiredtomato.burgered.item.BurgerItem
import net.wiredtomato.burgered.item.VanillaItemBurgerIngredientItem
import net.wiredtomato.burgered.item.components.BurgerComponent
import net.wiredtomato.burgered.item.components.VanillaBurgerIngredientComponent

object BurgeredItems {
    val TOP_BUN = register(
        "top_bun",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(1)
                .overSaturation(1.0)
                .modelHeight(2.0)
        )
    )

    val BOTTOM_BUN = register(
        "bottom_bun",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(1)
                .overSaturation(1.0)
                .modelHeight(2.0)
        )
    )

    val RAW_BEEF_PATTY = register(
        "raw_beef_patty",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(1)
                .overSaturation(2.0)
                .modelHeight(1.0)
                .statusEffect(StatusEffectInstance(StatusEffects.POISON, 200, 2), 0.25f)
        )
    )

    val BEEF_PATTY = register(
        "beef_patty",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(4)
                .overSaturation(8.0)
                .modelHeight(1.0)
        )
    )

    val CHEESE_SLICE = register(
        "cheese_slice",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(1)
                .overSaturation(0.25)
                .modelHeight(1.0)
        )
    )

    val LETTUCE = register(
        "lettuce",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(1)
                .overSaturation(0.25)
                .modelHeight(0.0)
        )
    )

    val EDIBLE_BOOK = register(
        "edible_book",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(7)
                .overSaturation(8.0)
                .modelHeight(4.0)
        )
    )

    val PICKLED_BEETS = register(
        "pickled_beets",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(2)
                .overSaturation(1.0)
                .modelHeight(0.0)
        )
    )

    val ESTROGEN_WAFFLE = register("estrogen_waffle", Item(Item.Settings()))

    val CUSTOM_BURGER_INGREDIENT = register(
        "custom_burger_ingredient",
        Item(Item.Settings())
    )

    val VANILLA_INGREDIENT = register(
        "vanilla_ingredient",
        VanillaItemBurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(2)
                .overSaturation(4.0)
                .modelHeight(1.0)
                .component(BurgeredDataComponents.VANILLA_BURGER_INGREDIENT, VanillaBurgerIngredientComponent.DEFAULT)
        )
    )

    val BURGER = register(
        "burger",
        BurgerItem(
            Item.Settings()
                .component(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        )
    )

    val BOOK_OF_BURGERS = register(
        "book_of_burgers",
        Item(Item.Settings())
    )

    val BURGER_STACKER = register("burger_stacker", BlockItem(BurgeredBlocks.BURGER_STACKER, Item.Settings()))
    val GRILL = register("grill", BlockItem(BurgeredBlocks.GRILL, Item.Settings()))

    fun <T : Item> register(name: String, item: T): T {
        return Registry.register(Registries.ITEM, Burgered.id(name), item)
    }
}