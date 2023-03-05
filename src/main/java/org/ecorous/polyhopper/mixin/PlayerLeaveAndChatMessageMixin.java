package org.ecorous.polyhopper.mixin;

import net.minecraft.network.message.SignedChatMessage;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.ecorous.polyhopper.MessageHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerLeaveAndChatMessageMixin {
	@Shadow
	public ServerPlayerEntity player;

	@Inject(
			method = "onDisconnected(Lnet/minecraft/text/Text;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/PlayerManager;broadcastSystemMessage(Lnet/minecraft/text/Text;Z)V",
					shift = At.Shift.AFTER
			)
	)
	private void polyhopper$onDisconnected(Text reason, CallbackInfo ci) {
		MessageHooks.INSTANCE.onPlayerDisconnected(player, reason);
	}

	@Inject(
			method = "sendChatMessage(Lnet/minecraft/network/message/SignedChatMessage;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/network/ServerPlayNetworkHandler;checkForSpam()V"
			)
	)
	private void polyhopper$sendChatMessage(SignedChatMessage signedChatMessage, CallbackInfo ci) {
		MessageHooks.INSTANCE.onChatMessageSent(player, signedChatMessage.getContent());
	}
}
