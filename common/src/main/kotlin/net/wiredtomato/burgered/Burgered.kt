package net.wiredtomato.burgered

import dev.architectury.registry.ReloadListenerRegistry
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.packs.PackType
import net.wiredtomato.burgered.api.data.burger.BurgerStackablesLoader
import net.wiredtomato.burgered.api.event.LivingEntityEvents
import net.wiredtomato.burgered.api.ingredient.BurgerIngredient
import net.wiredtomato.burgered.init.*
import org.slf4j.LoggerFactory

object Burgered {
    const val MOD_ID = "burgered"
    val LOGGER = LoggerFactory.getLogger(Burgered::class.java)

    fun init() {
        ReloadListenerRegistry.register(PackType.SERVER_DATA, BurgerStackablesLoader)
        BurgeredRegistries
        BurgeredBlocks.BLOCKS.register()
        BurgeredBlockEntities.BLOCK_ENTITIES.register()
        BurgeredDataComponents.DATA_COMPONENTS.register()
        BurgeredItems.ITEMS.register()
        BurgeredRecipes.RECIPES.register()
        BurgeredRecipes.Serializers.RECIPE_SERIALIZERS.register()

        LivingEntityEvents.ON_EAT.register onEat@ { entity, world, stack, component ->
            val burger = stack.get(BurgeredDataComponents.BURGER) ?: run {
                val item = stack.item
                if (item is BurgerIngredient) item.onEat(entity, world, stack, component)

                return@onEat
            }
            burger.onEat(entity, world, stack, component)
        }
    }

    fun modLoc(path: String) = ResourceLocation.fromNamespaceAndPath(MOD_ID, path)
}
