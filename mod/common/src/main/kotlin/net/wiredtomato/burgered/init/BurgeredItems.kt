package net.wiredtomato.burgered.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.Item
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings
import net.wiredtomato.burgered.item.BurgerIngredientItem
import net.wiredtomato.burgered.item.BurgerIngredientItem.BurgerIngredientProperties
import net.wiredtomato.burgered.item.BurgerItem
import net.wiredtomato.burgered.service.RegistryServiceImpl
import org.joml.Vector3d

object BurgeredItems {
    val TOP_BUN by registering(
        "top_bun",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(1)
            .saturation(1f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 2.0))
            .createFoodComponents()

    }

    val BOTTOM_BUN by registering(
        "bottom_bun",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(1)
            .saturation(1f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 2.0))
            .createFoodComponents()
    }

    val RAW_BEEF_PATTY by registering(
        "raw_beef_patty",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(1)
            .saturation(2f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 1.0))
            .singleStatusEffect(MobEffectInstance(MobEffects.POISON, 200, 2), 0.25f)
            .createFoodComponents()
    }

    val BEEF_PATTY by registering(
        "beef_patty",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(4)
            .saturation(8f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 1.0))
            .createFoodComponents()
    }

    val CHEESE_SLICE by registering(
        "cheese_slice",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(1)
            .saturation(0.25f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 1.0))
            .createFoodComponents()
    }

    val LETTUCE by registering(
        "lettuce",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(1)
            .saturation(0.25f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 0.0))
            .createFoodComponents()
    }

    val EDIBLE_BOOK by registering(
        "edible_book",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(7)
            .saturation(8f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 4.0))
            .createFoodComponents()
    }

    val PICKLED_BEETS by registering(
        "pickled_beets",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(2)
            .saturation(1f)
            .renderSettings(IngredientRenderSettings.ItemModel3d(Vector3d(1.0), Vector3d(), 0.0))
            .createFoodComponents()
    }

    val ESTROGEN_WAFFLE by registering(
        "estrogen_waffle",
        ::BurgerIngredientItem
    ) {
        BurgerIngredientProperties()
            .hunger(2)
            .saturation(1f)
            .renderSettings(IngredientRenderSettings.ItemModel2d(Vector3d(1.0), Vector3d()))
            .createFoodComponents()
    }

    val BURGER by registering(
        "burger",
        ::BurgerItem
    ) {
        Item.Properties().stacksTo(64)
    }

    val BOOK_OF_BURGERS by registering(
        "book_of_burgers",
        ::Item
    ) { Item.Properties() }

    val BURGER_STACKER by registering(
        "burger_stacker",
        { BlockItem(BurgeredBlocks.BURGER_STACKER, it) }
    ) { Item.Properties() }

    val GRILL by registering(
        "grill",
        { BlockItem(BurgeredBlocks.GRILL, it) }
    ) { Item.Properties() }

    fun <T : Item, P : Item.Properties> registering(
        name: String,
        itemConstructor: (P) -> T,
        properties: KSupplier<P>
    ): KSupplier<T> {
        val id = Burgered.modLoc(name)
        return RegistryServiceImpl.register(BuiltInRegistries.ITEM, id) {
            @Suppress("UNCHECKED_CAST")
            itemConstructor(properties.get().setId(ResourceKey.create(Registries.ITEM, id)) as P)
        }
    }

    fun items(): List<Item> {
        return BuiltInRegistries.ITEM.entrySet()
            .filter { it.component1().location().namespace == Burgered.MOD_ID }
            .map { it.component2() }
    }
}