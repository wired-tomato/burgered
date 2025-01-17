package net.wiredtomato.burgered.init

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.crafting.Recipe
import net.minecraft.world.item.crafting.RecipeSerializer
import net.minecraft.world.item.crafting.RecipeType
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.function.KSupplier
import net.wiredtomato.burgered.recipe.GrillingRecipe
import net.wiredtomato.burgered.service.RegistryServiceImpl

object BurgeredRecipes {
    val GRILLING: RecipeType<GrillingRecipe> by registering("grilling")

    fun <T, V : Recipe<T>> registering(path: String): KSupplier<RecipeType<V>> {
        val id = Burgered.modLoc(path)
        return RegistryServiceImpl.register(BuiltInRegistries.RECIPE_TYPE, id) {
            object : RecipeType<V> {
                override fun toString(): String {
                    return id.toString()
                }
            }
        }
    }

    object Serializers {
        val GRILLING by registering("grilling", GrillingRecipe.SERIALIZER)

        fun <T, V : RecipeSerializer<T>> registering(path: String, serializer: V): KSupplier<V> {
            return RegistryServiceImpl.register(BuiltInRegistries.RECIPE_SERIALIZER, Burgered.modLoc(path)) { serializer }
        }
    }
}
