package net.wiredtomato.burgered.init

import dev.architectury.registry.registries.DeferredRegister
import net.minecraft.core.registries.Registries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.registry.RegistryDelegate
import net.wiredtomato.burgered.api.registry.registered
import net.wiredtomato.burgered.recipe.GrillingRecipe
import net.wiredtomato.burgered.recipe.VanillaBurgerIngredientRecipe

object BurgeredRecipes {
    val RECIPES = DeferredRegister.create(Burgered.MOD_ID, Registries.RECIPE_TYPE)

    val GRILLING: RecipeType<GrillingRecipe> by registering("grilling")

    fun <T, V : Recipe<T>> registering(path: String): RegistryDelegate<BurgeredRecipes, RecipeType<V>> {
        val id = Burgered.modLoc(path)
        return registered(RECIPES.register(id) {
            object : RecipeType<V> {
                override fun toString(): String {
                    return id.toString()
                }
            }
        })
    }

    object Serializers {
        val RECIPE_SERIALIZERS = DeferredRegister.create(Burgered.MOD_ID, Registries.RECIPE_SERIALIZER)

        val VANILLA_BURGER_INGREDIENT by registering("vanilla_burger_ingredient", VanillaBurgerIngredientRecipe.SERIALIZER)
        val GRILLING by registering("grilling", GrillingRecipe.SERIALIZER)

        fun <T, V : RecipeSerializer<T>> registering(path: String, serializer: V): RegistryDelegate<Serializers, V> {
            return registered(RECIPE_SERIALIZERS.register(Burgered.modLoc(path)) { serializer })
        }
    }
}
