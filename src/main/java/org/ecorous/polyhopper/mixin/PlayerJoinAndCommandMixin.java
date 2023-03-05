package org.ecorous.polyhopper.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.network.message.MessageType;
import net.minecraft.network.message.SignedChatMessage;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.ecorous.polyhopper.MessageHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerJoinAndCommandMixin {
	@Inject(
			method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/PlayerManager;broadcastSystemMessage(Lnet/minecraft/text/Text;Z)V",
					shift = At.Shift.AFTER
			)
	)
	private void polyhopper$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {
		MessageHooks.INSTANCE.onPlayerConnected(player);
	}

	@Inject(
			method = "sendSignedMessage(Lnet/minecraft/network/message/SignedChatMessage;Lnet/minecraft/server/command/ServerCommandSource;Lnet/minecraft/network/message/MessageType$Parameters;)V",
			at = @At("TAIL")
	)
	private void polyhopper$sendChatMessage(SignedChatMessage message, ServerCommandSource commandSource, MessageType.Parameters parameters, CallbackInfo ci) {
		Identifier messageType = commandSource.getRegistryManager().get(RegistryKeys.MESSAGE_TYPE).getId(parameters.messageType());
		if (MessageType.EMOTE_COMMAND.getValue().equals(messageType)) {
			MessageHooks.INSTANCE.onMeCommand(commandSource.getPlayer(), message.getContent());
		} else if (MessageType.SAY_COMMAND.getValue().equals(messageType)) {
			MessageHooks.INSTANCE.onSayCommand(commandSource.getPlayer(), message.getContent());
		}
	}
}
