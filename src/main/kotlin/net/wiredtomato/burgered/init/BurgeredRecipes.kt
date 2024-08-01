package net.wiredtomato.burgered.init

import net.minecraft.recipe.Recipe
import net.minecraft.recipe.RecipeSerializer
import net.minecraft.recipe.RecipeType
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.recipe.GrillingRecipe
import net.wiredtomato.burgered.recipe.VanillaBurgerIngredientRecipe

object BurgeredRecipes {
    init { Serializers }

    val GRILLING: RecipeType<GrillingRecipe> = register("grilling")

    fun <T, V : Recipe<T>> register(path: String): RecipeType<V> {
        val id = Burgered.id(path)
        return Registry.register(Registries.RECIPE_TYPE, id, object : RecipeType<V> {
            override fun toString(): String {
                return id.toString()
            }
        })
    }

    object Serializers {
        val VANILLA_BURGER_INGREDIENT = register("vanilla_burger_ingredient", VanillaBurgerIngredientRecipe.SERIALIZER)
        val GRILLING = register("grilling", GrillingRecipe.SERIALIZER)

        fun <T, V : RecipeSerializer<T>> register(path: String, serializer: V): V {
            return Registry.register(Registries.RECIPE_SERIALIZER, Burgered.id(path), serializer)
        }
    }
}