package net.wiredtomato.burgered.neoforge.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.wiredtomato.burgered.platform.DynamicItemRendererRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {
    @Inject(method = "renderByItem", at = @At("HEAD"), cancellable = true)
    private void render(ItemStack arg, ItemDisplayContext arg2, PoseStack arg3, MultiBufferSource arg4, int i, int j, CallbackInfo ci) {
        var renderer = DynamicItemRendererRegistry.getRenderer(arg.getItem());
        if (renderer != null) {
            renderer.render(arg, arg2, arg3, arg4, i, j);
            ci.cancel();
        }
    }
}
