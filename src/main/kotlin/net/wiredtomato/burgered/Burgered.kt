package net.wiredtomato.burgered

import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.api.event.LivingEntityEvents
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.init.*
import net.wiredtomato.burgered.recipe.VanillaBurgerIngredientRecipe
import org.slf4j.LoggerFactory

object Burgered {
    const val MOD_ID = "burgered"
    val LOGGER = LoggerFactory.getLogger(MOD_ID)

    fun commonInit() {
        BurgeredBlocks
        BurgeredBlockEntities
        BurgeredItems
        BurgeredItemTags
        BurgeredDataComponents
        BurgeredTabs

        LivingEntityEvents.ON_EAT.register onEat@ { entity, world, stack, component ->
            val burger = stack.get(BurgeredDataComponents.BURGER) ?: run {
                val item = stack.item
                if (item is BurgerIngredient) item.onEat(entity, world, stack, component)

                return@onEat
            }
            burger.onEat(entity, world, stack, component)
        }

        Registry.register(Registries.RECIPE_SERIALIZER, id("vanilla_burger_ingredient"), VanillaBurgerIngredientRecipe.SERIALIZER)
    }

    fun id(path: String) = Identifier.of(MOD_ID, path)
}