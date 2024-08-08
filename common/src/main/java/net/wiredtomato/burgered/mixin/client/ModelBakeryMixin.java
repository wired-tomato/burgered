package net.wiredtomato.burgered.mixin.client;

import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.resources.model.BlockStateModelLoader;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.profiling.ProfilerFiller;
import net.wiredtomato.burgered.client.ktmixin.ModelBakeryKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Map;

@Mixin(ModelBakery.class)
public abstract class ModelBakeryMixin {
    @Shadow protected abstract void registerModel(ModelResourceLocation modelResourceLocation, UnbakedModel unbakedModel);

    @Inject(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/resources/model/BlockStateModelLoader;getModelGroups()Lit/unimi/dsi/fastutil/objects/Object2IntMap;"))
    private void loadCustomModels(BlockColors blockColors, ProfilerFiller profilerFiller, Map<ResourceLocation, BlockModel> map, Map<ResourceLocation, List<BlockStateModelLoader.LoadedJson>> map2, CallbackInfo ci) {
        ModelBakeryKt.loadCustomModels((ModelBakery) (Object) this, blockColors, profilerFiller, map, map2, this::registerModel);
    }
}
