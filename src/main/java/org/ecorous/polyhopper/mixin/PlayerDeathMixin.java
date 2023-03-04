package org.ecorous.polyhopper.mixin;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.ecorous.polyhopper.MessageHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public class PlayerDeathMixin {
	@Inject(
			method = "onDeath(Lnet/minecraft/entity/damage/DamageSource;)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/network/ServerPlayerEntity;emitGameEvent(Lnet/minecraft/world/event/GameEvent;)V",
					shift = At.Shift.AFTER
			)
	)
	private void polyhopper$onDeath(DamageSource source, CallbackInfo ci) {
		ServerPlayerEntity self = (ServerPlayerEntity) (Object) this;
		Text deathMessage = self.getDamageTracker().getDeathMessage();
		MessageHooks.INSTANCE.onPlayerDeath(self, deathMessage);
	}
}
