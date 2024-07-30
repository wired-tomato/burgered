package net.wiredtomato.burgered.data.gen.provider

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider
import net.minecraft.block.Block
import net.minecraft.item.Item
import net.minecraft.registry.HolderLookup
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKeys
import net.minecraft.util.Identifier
import net.wiredtomato.burgered.Burgered
import net.wiredtomato.burgered.data.CommonText
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

        translationBuilder.add(CommonText.INGREDIENTS, "Ingredients: ")
        translationBuilder.add(CommonText.CANT_BE_PUT_ON_BURGER, "You can't put %s on this burger!")
    }

    private fun genLang(identifier: Identifier): String =
        identifier.path.titleCase()

    private fun String.titleCase(): String {
        return split("_").joinToString(" ") { it.replaceFirstChar(Char::uppercaseChar) }
    }

    val Item.id get() = Registries.ITEM.getId(this)
    val Block.id get() = Registries.BLOCK.getId(this)
}