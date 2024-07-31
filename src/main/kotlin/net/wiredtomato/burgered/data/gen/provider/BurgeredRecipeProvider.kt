package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.RecipesProvider
import net.minecraft.data.server.recipe.RecipeExporter
import net.minecraft.data.server.recipe.ShapedRecipeJsonFactory
import net.minecraft.data.server.recipe.ShapelessRecipeJsonFactory
import net.minecraft.data.server.recipe.TransformSmithingRecipeJsonFactory
import net.minecraft.item.Item
import net.minecraft.item.Items
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeCategory
import net.minecraft.registry.HolderLookup
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredItems
import java.util.concurrent.CompletableFuture

class BurgeredRecipeProvider(
    output: FabricDataOutput,
    registriesFuture: CompletableFuture<HolderLookup.Provider>
) : FabricRecipeProvider(output, registriesFuture) {
    override fun generateRecipes(exporter: RecipeExporter) {
        burgerSmithingRecipe(Items.BOOK, BurgeredItems.EDIBLE_BOOK).offerTo(exporter, Burgered.id("edible_book_smithing"))

        ShapelessRecipeJsonFactory.create(RecipeCategory.MISC, BurgeredItems.BOOK_OF_BURGERS)
            .ingredient(BurgeredItems.BURGER)
            .ingredient(Items.BOOK)
            .criterion(hasItem(Items.BOOK), RecipesProvider.conditionsFromItem(Items.BOOK))
            .offerTo(exporter)

        ShapedRecipeJsonFactory.create(RecipeCategory.MISC, BurgeredItems.BOOK_OF_BURGERS, 8)
            .pattern("DbD")
            .pattern("DBD")
            .pattern("DDD")
            .ingredient('D', Items.DIAMOND)
            .ingredient('b', BurgeredItems.BOOK_OF_BURGERS)
            .ingredient('B', BurgeredItems.BURGER)
            .criterion(hasItem(BurgeredItems.BOOK_OF_BURGERS), RecipesProvider.conditionsFromItem(BurgeredItems.BOOK_OF_BURGERS))
            .offerTo(exporter, Burgered.id("book_of_burgers_duplicate"))
    }

    fun burgerSmithingRecipe(base: Item, result: Item): TransformSmithingRecipeJsonFactory {
        return TransformSmithingRecipeJsonFactory(
            Ingredient.ofItems(BurgeredItems.BOOK_OF_BURGERS),
            Ingredient.ofItems(base),
            Ingredient.ofItems(BurgeredItems.BURGER),
            RecipeCategory.FOOD,
            result
        ).criterion(hasItem(BurgeredItems.BOOK_OF_BURGERS), RecipesProvider.conditionsFromItem(BurgeredItems.BOOK_OF_BURGERS))
    }
}