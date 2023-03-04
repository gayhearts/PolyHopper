package org.ecorous.polyhopper.mixin;

import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerManager.class)
public class PlayerJoinMessageMixin {

	@Inject(
			method = "onPlayerConnect(Lnet/minecraft/network/ClientConnection;Lnet/minecraft/server/network/ServerPlayerEntity;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/PlayerManager;m_bgctehjy(Lnet/minecraft/text/Text;Z)V",
					shift = At.Shift.AFTER
			)
	)
	private void polyhopper$onPlayerConnect(ClientConnection connection, ServerPlayerEntity player, CallbackInfo ci) {

	}
}
