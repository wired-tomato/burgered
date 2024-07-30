package net.wiredtomato.burgered.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.component.DataComponentMap;
import net.minecraft.item.ItemStack;
import net.wiredtomato.burgered.psuedomixin.ItemStackFoodComponentKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(ItemStack.class)
public class ItemStackMixin {
    @ModifyReturnValue(method = "getComponents", at = @At("RETURN"))
    public DataComponentMap changeFoodComponent(DataComponentMap original) {
        return ItemStackFoodComponentKt.modifyFoodComponent(original);
    }
}
