package org.ecorous.polyhopper.mixin;

import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayNetworkHandler.class)
public class PlayerLeaveMessageMixin {

	@Shadow
	public ServerPlayerEntity player;

	@Inject(
			method = "onDisconnected(Lnet/minecraft/text/Text;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/PlayerManager;m_bgctehjy(Lnet/minecraft/text/Text;Z)V",
					shift = At.Shift.AFTER
			)
	)
	private void polyhopper$onDisconnect(Text reason, CallbackInfo ci) {

	}
}
