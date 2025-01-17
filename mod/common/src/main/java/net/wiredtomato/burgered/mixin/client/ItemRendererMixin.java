package net.wiredtomato.burgered.mixin.client;

import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.wiredtomato.burgered.client.rendering.item.ItemRendererAccess;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin implements ItemRendererAccess {
    @Shadow private ItemModelResolver resolver;

    @Override
    public @NotNull ItemModelResolver burgered$getModelResolver() {
        return resolver;
    }
}
