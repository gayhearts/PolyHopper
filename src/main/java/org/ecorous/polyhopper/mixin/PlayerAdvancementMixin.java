package org.ecorous.polyhopper.mixin;

import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.PlayerAdvancementTracker;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import org.ecorous.polyhopper.MessageHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerAdvancementTracker.class)
public class PlayerAdvancementMixin {
	@Shadow
	private ServerPlayerEntity owner;

	@Inject(
			method = "grantCriterion(Lnet/minecraft/advancement/Advancement;Ljava/lang/String;)Z",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/advancement/AdvancementRewards;apply(Lnet/minecraft/server/network/ServerPlayerEntity;)V",
					shift = At.Shift.AFTER
			)
	)
	private void polyhopper$grantCriterion(Advancement advancement, String criterionName, CallbackInfoReturnable<Boolean> cir) {
		if (advancement.getDisplay() != null && advancement.getDisplay().shouldAnnounceToChat()) {
			Text advancementMessage = Text.translatable("chat.type.advancement." + advancement.getDisplay().getFrame().getId(), owner.getDisplayName(), advancement.toHoverableText());
			MessageHooks.INSTANCE.onAdvancement(owner, advancementMessage, advancement.getDisplay().getDescription());
		}
	}
}
