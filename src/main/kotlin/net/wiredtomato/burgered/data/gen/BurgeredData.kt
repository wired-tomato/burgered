package net.wiredtomato.burgered.data.gen

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator
import net.wiredtomato.burgered.data.gen.provider.*

object BurgeredData : DataGeneratorEntrypoint {
    override fun onInitializeDataGenerator(fabricDataGenerator: FabricDataGenerator) {
        val pack = fabricDataGenerator.createPack()
        pack.addProvider(::BurgeredModelProvider)
        pack.addProvider(::BurgeredEnUsLangProvider)
        pack.addProvider(::BurgeredRecipeProvider)
        pack.addProvider(::BurgeredStackableProvider)
    }
}