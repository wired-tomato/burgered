package net.wiredtomato.burgered.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.wiredtomato.burgered.api.event.LivingEntityEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "eat", at = @At("TAIL"))
    public void onEat(World world, ItemStack stack, FoodComponent food, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntityEvents.INSTANCE.getON_EAT().invoker().onEat((LivingEntity) (Object) this, world, stack, food);
    }
}
