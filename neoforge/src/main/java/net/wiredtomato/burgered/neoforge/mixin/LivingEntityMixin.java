package net.wiredtomato.burgered.neoforge.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.wiredtomato.burgered.api.event.LivingEntityEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "eat(Lnet/minecraft/world/level/Level;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/food/FoodProperties;)Lnet/minecraft/world/item/ItemStack;", at = @At("TAIL"))
    public void onEat(Level world, ItemStack stack, FoodProperties food, CallbackInfoReturnable<ItemStack> cir) {
        LivingEntityEvents.INSTANCE.getON_EAT().invoker().onEat((LivingEntity) (Object) this, world, stack, food);
    }
}
