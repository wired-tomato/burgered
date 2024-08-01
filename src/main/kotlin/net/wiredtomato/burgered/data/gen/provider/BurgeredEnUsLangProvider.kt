package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.ItemGroup
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.data.text.CommonText
import net.wiredtomato.burgered.util.id
import java.util.concurrent.CompletableFuture
import kotlin.jvm.optionals.getOrElse

class BurgeredEnUsLangProvider(
    output: FabricDataOutput,
    lookup: CompletableFuture<HolderLookup.Provider>
) : FabricLanguageProvider(output, lookup) {
    override fun generateTranslations(registryLookup: HolderLookup.Provider, translationBuilder: TranslationBuilder) {
        registryLookup.getLookup(RegistryKeys.ITEM).getOrElse { return }
            .holders()
            .filter { it.registryKey.value.namespace == Burgered.MOD_ID }
            .map { it.value() }
            .forEach { translationBuilder.add(it, genLang(it.id)) }

        registryLookup.getLookup(RegistryKeys.ITEM_GROUP).getOrElse { return }
            .holders()
            .filter { it.registryKey.value.namespace == Burgered.MOD_ID }
            .map { it.value() }
            .forEach { translationBuilder.add(it.id.toTranslationKey("itemgroup"), genLang(it.id)) }

        translationBuilder.add(CommonText.INGREDIENTS, "Ingredients: ")
        translationBuilder.add(CommonText.CANT_BE_PUT_ON_BURGER, "You can't put %s on this burger!")
        translationBuilder.add(CommonText.BURGER_MAX_SIZE, "Sorry, I can't let you put anymore ingredient on this burger.")
        translationBuilder.add(CommonText.SLOPPINESS, "Sloppiness: %s")

        translationBuilder.add("emi.category.burgered.grilling", "Grilling")

        translationBuilder.add("${Burgered.MOD_ID}.midnightlib.maxRenderedBurgerIngredients", "Max Rendered Burger Ingredients")
        translationBuilder.add("${Burgered.MOD_ID}.midnightlib.maxRenderedBurgerIngredients.tooltip", "Maximum number of ingredients to render per burger.")
    }

    private fun genLang(identifier: Identifier): String =
        identifier.path.titleCase()

    private fun String.titleCase(): String {
        return split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }

    val ItemGroup.id get() = Registries.ITEM_GROUP.getId(this)!!
    val Block.id get() = Registries.BLOCK.getId(this)
}