package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.advancements.AdvancementRequirements
import net.minecraft.advancements.AdvancementRewards
import net.minecraft.advancements.Criterion
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger
import net.minecraft.core.HolderLookup
import net.minecraft.core.registries.Registries
import net.minecraft.data.PackOutput
import net.minecraft.data.recipes.*
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.tags.ItemTags
import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Items
import net.minecraft.world.item.crafting.CookingBookCategory
import net.minecraft.world.item.crafting.Ingredient
import net.minecraft.world.item.crafting.Recipe
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.util.id
import net.wiredtomato.burgered.init.BurgeredItems
import net.wiredtomato.burgered.recipe.GrillingRecipe
import java.util.concurrent.CompletableFuture

class BurgeredRecipeProvider(
    registries: HolderLookup.Provider,
    output: RecipeOutput,
) : RecipeProvider(registries, output) {
    override fun buildRecipes() {
        SmithingTransformRecipeBuilder(
            Ingredient.of(BurgeredItems.BOOK_OF_BURGERS),
            Ingredient.of(Items.BOOK),
            Ingredient.of(Items.LEATHER),
            RecipeCategory.FOOD,
            BurgeredItems.EDIBLE_BOOK
        ).unlocks(
            getHasName(BurgeredItems.BOOK_OF_BURGERS),
            has(BurgeredItems.BOOK_OF_BURGERS)
        ).save(output, key("edible_book"))

        ShapelessRecipeBuilder.shapeless(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, BurgeredItems.BOOK_OF_BURGERS)
            .requires(BurgeredItems.BURGER)
            .requires(Items.BOOK)
            .unlockedBy(getHasName(Items.BOOK), has(Items.BOOK))
            .save(output)

        ShapedRecipeBuilder.shaped(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, BurgeredItems.BOOK_OF_BURGERS, 32)
            .pattern("DbD")
            .pattern("DBD")
            .pattern("DDD")
            .define('D', Items.DIAMOND)
            .define('b', BurgeredItems.BOOK_OF_BURGERS)
            .define('B', BurgeredItems.BURGER)
            .unlockedBy(
                getHasName(BurgeredItems.BOOK_OF_BURGERS),
                has(BurgeredItems.BOOK_OF_BURGERS)
            )
            .save(output, key("book_of_burgers_duplicate"))

        ShapedRecipeBuilder.shaped(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, BurgeredItems.BURGER_STACKER)
            .pattern(" F ")
            .pattern(" F ")
            .pattern("SSS")
            .define('F', ItemTags.WOODEN_FENCES)
            .define('S', ItemTags.WOODEN_SLABS)
            .unlockedBy(
                getHasName(BurgeredItems.BOTTOM_BUN),
                has(BurgeredItems.BOTTOM_BUN)
            )
            .save(output)

        ShapedRecipeBuilder.shaped(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, BurgeredItems.GRILL)
            .pattern("III")
            .pattern("I I")
            .pattern("I I")
            .define('I', Items.IRON_BARS)
            .unlockedBy(
                getHasName(BurgeredItems.BOTTOM_BUN),
                has(BurgeredItems.BOTTOM_BUN)
            )
            .save(output)

        ShapedRecipeBuilder.shaped(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.MISC, BurgeredItems.ESTROGEN_WAFFLE)
            .pattern(" C ")
            .pattern("EME")
            .pattern("MEM")
            .define('C', ItemTags.CANDLES)
            .define('E', Items.EGG)
            .define('M', Items.MILK_BUCKET)
            .unlockedBy(getHasName(Items.MILK_BUCKET), has(Items.MILK_BUCKET))
            .save(output)

        ShapelessRecipeBuilder.shapeless(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.FOOD, BurgeredItems.RAW_BEEF_PATTY, 4)
            .requires(Items.BEEF)
            .requires(Items.STICK)
            .unlockedBy(getHasName(Items.BEEF), has(Items.BEEF))
            .save(output)

        ShapelessRecipeBuilder.shapeless(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.FOOD, BurgeredItems.PICKLED_BEETS, 4)
            .requires(Items.SEA_PICKLE)
            .requires(Items.BEETROOT)
            .unlockedBy(getHasName(Items.BEETROOT), has(Items.BEETROOT))
            .save(output)

        ShapelessRecipeBuilder.shapeless(registries.lookupOrThrow(Registries.ITEM), RecipeCategory.FOOD, BurgeredItems.LETTUCE, 4)
            .requires(Items.SEA_PICKLE)
            .requires(Items.STICK)
            .unlockedBy(getHasName(Items.SEA_PICKLE), has(Items.SEA_PICKLE))
            .save(output)

        SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(Items.BREAD),
            RecipeCategory.FOOD,
            BurgeredItems.TOP_BUN,
            2
        ).unlockedBy(
            getHasName(Items.BREAD),
            has(Items.BREAD)
        ).save(output, key("top_bun_stonecutting"))

        SingleItemRecipeBuilder.stonecutting(
            Ingredient.of(Items.BREAD),
            RecipeCategory.FOOD,
            BurgeredItems.BOTTOM_BUN,
            2
        ).unlockedBy(
            getHasName(Items.BREAD),
            has(Items.BREAD)
        ).save(output, key("bottom_bun_stonecutting"))

        grillingRecipe(
            output,
            CookingBookCategory.FOOD,
            Ingredient.of(BurgeredItems.RAW_BEEF_PATTY),
            ItemStack.EMPTY,
            BurgeredItems.BEEF_PATTY.defaultInstance,
            7f,
            100,
            getHasName(BurgeredItems.RAW_BEEF_PATTY),
            has(BurgeredItems.RAW_BEEF_PATTY)
        )

        grillingRecipe(
            output,
            CookingBookCategory.FOOD,
            Ingredient.of(Items.MILK_BUCKET),
            Items.BUCKET.defaultInstance,
            ItemStack(BurgeredItems.CHEESE_SLICE, 4),
            7f,
            100,
            getHasName(Items.MILK_BUCKET),
            has(Items.MILK_BUCKET)
        )
    }

    fun key(id: ResourceLocation): ResourceKey<Recipe<*>> {
        return ResourceKey.create(Registries.RECIPE, id)
    }

    fun key(path: String): ResourceKey<Recipe<*>> {
        return key(Burgered.modLoc(path))
    }

    fun grillingRecipe(
        exporter: RecipeOutput,
        category: CookingBookCategory,
        ingredient: Ingredient,
        transform: ItemStack,
        result: ItemStack,
        experience: Float,
        cookingTime: Int,
        unlockedByName: String,
        unlockedBy: Criterion<*>,
        recipeId: ResourceLocation = result.item.id
    ) {
        val recipeKey = key(recipeId)
        val recipe = GrillingRecipe("", category, ingredient, transform, result, experience, cookingTime)
        val builder = exporter.advancement().addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(recipeKey)).rewards(
            AdvancementRewards.Builder.recipe(recipeKey)
        ).requirements(AdvancementRequirements.Strategy.OR)
        builder.addCriterion(unlockedByName, unlockedBy)

        exporter.accept(recipeKey, recipe, builder.build(recipeId.withPrefix("recipes/" + category.serializedName + "/")))
    }

    fun burgerSmithingRecipe(base: Item, result: Item): SmithingTransformRecipeBuilder {
        return SmithingTransformRecipeBuilder(
            Ingredient.of(BurgeredItems.BOOK_OF_BURGERS),
            Ingredient.of(base),
            Ingredient.of(BurgeredItems.BURGER),
            RecipeCategory.FOOD,
            result
        ).unlocks(
            getHasName(BurgeredItems.BOOK_OF_BURGERS),
            has(BurgeredItems.BOOK_OF_BURGERS)
        )
    }

    class Runner(packOutput: PackOutput, registries: CompletableFuture<HolderLookup.Provider>) : RecipeProvider.Runner(packOutput, registries) {
        override fun createRecipeProvider(
            p0: HolderLookup.Provider,
            p1: RecipeOutput
        ): RecipeProvider {
            return BurgeredRecipeProvider(p0, p1)
        }

        override fun getName(): String {
            return "BurgeredRecipes"
        }
    }
}
