package net.wiredtomato.burgered.init

import net.minecraft.item.BlockItem
import net.minecraft.item.Item
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.item.BurgerIngredientItem
import net.wiredtomato.burgered.item.BurgerIngredientItem.BurgerIngredientSettings
import net.wiredtomato.burgered.item.BurgerItem
import net.wiredtomato.burgered.item.components.BurgerComponent

object BurgeredItems {
    val BURGER = register(
        "burger",
        BurgerItem(
            Item.Settings()
                .component(BurgeredDataComponents.BURGER, BurgerComponent.DEFAULT)
        )
    )

    val TOP_BUN = register(
        "top_bun",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(1)
                .overSaturation(1.0)
                .modelHeight(1.5)
        )
    )

    val BOTTOM_BUN = register(
        "bottom_bun",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(1)
                .overSaturation(1.0)
                .modelHeight(1.5)
        )
    )

    val BEEF_PATTY = register(
        "beef_patty",
        BurgerIngredientItem(
            BurgerIngredientSettings()
                .saturation(2)
                .overSaturation(4.0)
                .modelHeight(1.5)
        )
    )

    val BURGER_STACKER = register("burger_stacker", BlockItem(BurgeredBlocks.BURGER_STACKER, Item.Settings()))

    fun <T : Item> register(name: String, item: T): T {
        return Registry.register(Registries.ITEM, Burgered.id(name), item)
    }
}