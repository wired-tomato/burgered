package net.wiredtomato.burgered.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.commands.ReloadCommand;
import net.wiredtomato.burgered.ktmixin.SendUpdateKt;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

@Mixin(ReloadCommand.class)
public abstract class ReloadCommandMixin {

    @WrapOperation(
            method = "reloadPacks",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/MinecraftServer;reloadResources(Ljava/util/Collection;)Ljava/util/concurrent/CompletableFuture;")
    )
    private static CompletableFuture<Void> sendUpdate(MinecraftServer server, Collection<String> strings, Operation<CompletableFuture<Void>> operation) {
        return SendUpdateKt.sendUpdate(server, strings, operation);
    }
}
