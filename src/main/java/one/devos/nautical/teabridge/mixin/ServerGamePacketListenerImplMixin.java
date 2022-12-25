package one.devos.nautical.teabridge.mixin;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import one.devos.nautical.teabridge.Config;
import one.devos.nautical.teabridge.discord.Discord;
import one.devos.nautical.teabridge.duck.PlayerWebHook;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow @Final private ServerPlayer player;

    @Inject(method = "broadcastChatMessage", at = @At("RETURN"))
    private void teabridge$mirrorChatMessage(PlayerChatMessage message, CallbackInfo ci) {
        ((PlayerWebHook) player).send(message);
    }

    @ModifyArg(method = "onDisconnect", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/players/PlayerList;broadcastSystemMessage(Lnet/minecraft/network/chat/Component;Z)V"), index = 0)
    private Component teabridge$mirrorLeaveMessage(Component leaveMessage) {
        if (!Config.INSTANCE.game.mirrorDeath) return leaveMessage;
        Discord.send(leaveMessage.getString());
        return leaveMessage;
    }
}
