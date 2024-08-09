package net.wiredtomato.burgered.neoforge.data.gen.provider

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.data.PackOutput
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.CreativeModeTab
import net.minecraft.world.level.block.Block
import net.neoforged.neoforge.common.data.LanguageProvider
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.data.text.CommonText
import net.wiredtomato.burgered.util.id

class BurgeredEnUsLangProvider(output: PackOutput) : LanguageProvider(output, Burgered.MOD_ID, "en_us") {
    override fun addTranslations() {
        BuiltInRegistries.ITEM
            .holders()
            .filter { it.key().location().namespace == Burgered.MOD_ID }
            .map { it.value() }
            .forEach { add(it, genLang(it.id)) }

        add(CommonText.INGREDIENTS, "Ingredients: ")
        add(CommonText.CANT_BE_PUT_ON_BURGER, "You can't put %s on this burger!")
        add(CommonText.BURGER_MAX_SIZE, "Sorry, I can't let you put anymore ingredient on this burger.")
        add(CommonText.SLOPPINESS, "Sloppiness: %s")

        add("emi.category.burgered.grilling", "Grilling")

        add("config.burgered.client.title", "Burgered Client Config")
        add("config.burgered.client.category.rendering", "Rendering")
        add("config.burgered.client.category.rendering.description", "Rendering settings")
        add("config.burgered.maxRenderedBurgerIngredients", "Maximum Rendered Burger Ingredients")
        add("config.burgered.maxRenderedBurgerIngredients.tooltip", "Maximum number of burger ingredients to render per burger")
        add("config.burgered.maxSloppinessRotationX", "Maximum Ingredient Rotation X")
        add("config.burgered.maxSloppinessRotationX.tooltip", "Maximum degrees to rotate an ingredient along X-axis")
        add("config.burgered.maxSloppinessRotationY", "Maximum Ingredient Rotation Y")
        add("config.burgered.maxSloppinessRotationY.tooltip", "Maximum degrees to rotate an ingredient along Y-axis")
        add("config.burgered.maxSloppinessRotationZ", "Maximum Ingredient Rotation Z")
        add("config.burgered.maxSloppinessRotationZ.tooltip", "Maximum degrees to rotate an ingredient along Z-axis")
        add("config.burgered.renderNoTransform", "Render No Transform")
        add("config.burgered.renderNoTransform.tooltip.line1", "Whether to always render ingredients with no default transformations")
        add("config.burgered.renderNoTransform.tooltip.line2", "DISABLING THIS MAY CAUSE RENDERING ARTIFACTING WITH SOME INGREDIENTS AND MOD ADDONS")
    }

    private fun genLang(identifier: ResourceLocation): String =
        identifier.path.titleCase()

    private fun String.titleCase(): String {
        return split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }

    val CreativeModeTab.id get() = BuiltInRegistries.CREATIVE_MODE_TAB.getKey(this)!!
    val Block.id get() = BuiltInRegistries.BLOCK.getKey(this)
}
