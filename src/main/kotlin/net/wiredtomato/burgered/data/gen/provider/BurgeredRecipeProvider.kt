package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.data.server.RecipesProvider
import net.minecraft.data.server.recipe.RecipeExporter
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