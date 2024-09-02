package net.wiredtomato.burgered.ktmixin

import com.llamalad7.mixinextras.injector.wrapoperation.Operation
import dev.architectury.networking.NetworkManager
import net.minecraft.server.MinecraftServer
import net.wiredtomato.burgered.api.data.burger.BurgerStackables
import net.wiredtomato.burgered.networking.StackableSyncPacket
import java.util.concurrent.CompletableFuture

fun sendUpdate(server: MinecraftServer, strings: Collection<String>, original: Operation<CompletableFuture<Void>>): CompletableFuture<Void> {
    val future = original.call(server, strings)

    return future.thenRun { NetworkManager.sendToPlayers(server.playerList.players, StackableSyncPacket(BurgerStackables.toList())) }
}
