package net.wiredtomato.burgered.neoforge.data.gen

import net.minecraft.core.RegistrySetBuilder
import net.minecraft.resources.ResourceKey
import net.minecraft.world.item.Items
import net.neoforged.bus.api.SubscribeEvent
import net.neoforged.fml.common.EventBusSubscriber
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider
import net.neoforged.neoforge.data.event.GatherDataEvent
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.api.burger.ingredient.BurgerIngredient
import net.wiredtomato.burgered.api.registry.BurgeredRegistries
import net.wiredtomato.burgered.api.rendering.IngredientRenderSettings
import net.wiredtomato.burgered.neoforge.data.gen.provider.*
import org.joml.Vector3d

@EventBusSubscriber(modid = Burgered.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
object BurgeredDataGen {
    @SubscribeEvent
    fun gatherDataCommon(event: GatherDataEvent.Server) {
        val generator = event.generator
        val lookupProvider = generator.addProvider(
            true,
        ) { output ->
            DatapackBuiltinEntriesProvider(
                output,
                event.lookupProvider,
                RegistrySetBuilder().also { builder -> generateBurgerIngredients(builder) },
                setOf(Burgered.MOD_ID)
            )
        }.registryProvider
        val output = generator.packOutput

        generator.addProvider(
            true,
            BurgeredEnUsLangProvider(output)
        )

        generator.addProvider(
            true,
            BurgeredRecipeProvider.Runner(output, lookupProvider)
        )

        generator.addProvider(
            true,
            BurgeredLootTableProvider(output, lookupProvider)
        )

        generator.addProvider(
            true,
            BurgeredBlockTagProvider(output, lookupProvider)
        )
    }

    @SubscribeEvent
    fun gatherDataClient(event: GatherDataEvent.Client) {
        val generator = event.generator
        val output = generator.packOutput

        generator.addProvider(
            true,
            BurgeredModelProvider(output)
        )
    }

    fun generateBurgerIngredients(builder: RegistrySetBuilder) {
        fun key(path: String): ResourceKey<BurgerIngredient.ItemDetached> {
            return ResourceKey.create(BurgeredRegistries.Keys.INGREDIENT, Burgered.modLoc(path))
        }

        builder.add(BurgeredRegistries.Keys.INGREDIENT) { bootstap ->
            bootstap.register(
                key("nether_wart"),
                BurgerIngredient.ItemDetached(
                    2,
                    4f,
                    listOf(),
                    IngredientRenderSettings.ItemModel2d(Vector3d(0.5), Vector3d()),
                    Items.NETHER_WART,
                )
            )
        }
    }
}
