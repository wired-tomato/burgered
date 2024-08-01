package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider
import net.minecraft.advancement.AdvancementCriterion
import net.minecraft.advancement.AdvancementRequirements
import net.minecraft.advancement.AdvancementRewards
import net.minecraft.advancement.criterion.RecipeUnlockedCriterionTrigger
import net.minecraft.data.server.RecipesProvider
import net.minecraft.data.server.recipe.*
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.item.Items
import net.minecraft.recipe.CookingCategory
import net.minecraft.recipe.Ingredient
import net.minecraft.recipe.RecipeCategory
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.tag.ItemTags
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.recipe.GrillingRecipe
import net.wiredtomato.burgered.util.id
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

        ShapedRecipeJsonFactory.create(RecipeCategory.MISC, BurgeredItems.BOOK_OF_BURGERS, 32)
            .pattern("DbD")
            .pattern("DBD")
            .pattern("DDD")
            .ingredient('D', Items.DIAMOND)
            .ingredient('b', BurgeredItems.BOOK_OF_BURGERS)
            .ingredient('B', BurgeredItems.BURGER)
            .criterion(hasItem(BurgeredItems.BOOK_OF_BURGERS), RecipesProvider.conditionsFromItem(BurgeredItems.BOOK_OF_BURGERS))
            .offerTo(exporter, Burgered.id("book_of_burgers_duplicate"))

        ShapedRecipeJsonFactory.create(RecipeCategory.MISC, BurgeredItems.BURGER_STACKER)
            .pattern(" F ")
            .pattern(" F ")
            .pattern("SSS")
            .ingredient('F', ItemTags.FENCES)
            .ingredient('S', ItemTags.SLABS)
            .criterion(hasItem(BurgeredItems.BOTTOM_BUN), RecipesProvider.conditionsFromItem(BurgeredItems.BOTTOM_BUN))
            .offerTo(exporter)

        ShapedRecipeJsonFactory.create(RecipeCategory.MISC, BurgeredItems.GRILL)
            .pattern("III")
            .pattern("I I")
            .pattern("I I")
            .ingredient('I', Items.IRON_BARS)
            .criterion(hasItem(BurgeredItems.BOTTOM_BUN), RecipesProvider.conditionsFromItem(BurgeredItems.BOTTOM_BUN))
            .offerTo(exporter)

        ShapelessRecipeJsonFactory.create(RecipeCategory.FOOD, BurgeredItems.RAW_BEEF_PATTY)
            .ingredient(Items.BEEF)
            .ingredient(ItemTags.SWORDS)
            .criterion(hasItem(Items.BEEF), RecipesProvider.conditionsFromItem(Items.BEEF))
            .offerTo(exporter)

        RecipesProvider.createStonecuttingRecipe(exporter, RecipeCategory.FOOD, BurgeredItems.TOP_BUN, Items.BREAD)
        RecipesProvider.createStonecuttingRecipe(exporter, RecipeCategory.FOOD, BurgeredItems.BOTTOM_BUN, Items.BREAD)
        grillingRecipe(
            exporter,
            Ingredient.ofItems(BurgeredItems.RAW_BEEF_PATTY),
            CookingCategory.FOOD,
            BurgeredItems.BEEF_PATTY.defaultStack,
            25f,
            100,
            hasItem(BurgeredItems.RAW_BEEF_PATTY),
            RecipesProvider.conditionsFromItem(BurgeredItems.RAW_BEEF_PATTY)
        )
    }

    fun grillingRecipe(
        exporter: RecipeExporter,
        ingredient: Ingredient,
        category: CookingCategory,
        result: ItemStack,
        experience: Float,
        cookingTime: Int,
        criterionName: String,
        criterion: AdvancementCriterion<*>,
        recipeId: Identifier = result.item.id
    ) {
        val recipe = GrillingRecipe("", category, ingredient, result, experience, cookingTime)
        val builder = exporter.accept().putCriteria("has_the_recipe", RecipeUnlockedCriterionTrigger.create(recipeId)).rewards(
            AdvancementRewards.Builder.recipe(recipeId)
        ).merger(AdvancementRequirements.RequirementMerger.ANY)
        builder.putCriteria(criterionName, criterion)

        exporter.accept(recipeId, recipe, builder.build(recipeId.withPrefix("recipes/" + category.asString() + "/")))
    }

    fun CookingRecipeJsonFactory.hasItem(item: Item) = this.criterion(RecipesProvider.hasItem(item), RecipesProvider.conditionsFromItem(item))

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