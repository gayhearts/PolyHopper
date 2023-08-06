package org.ecorous.polyhopper.mixin;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.EntitySelector;
import net.minecraft.command.argument.TextArgumentType;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.command.TellRawCommand;
import net.minecraft.text.Text;
import org.ecorous.polyhopper.MessageHooks;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TellRawCommand.class)
public class TellRawMixin {
	@Inject(
			method = "lambda$register$1(Lcom/mojang/brigadier/context/CommandContext;)I",
			at= @At(value = "CONSTANT", args = "intValue=0", ordinal = 0)
	)
	private static void polyhopper$register(CommandContext<ServerCommandSource> context, CallbackInfoReturnable<Integer> cir) {
		EntitySelector selector = context.getArgument("targets", EntitySelector.class);
		// Currently @a is the only @ selector with no limit & can only target players.
		if (selector.usesAt() && selector.getLimit() == Integer.MAX_VALUE && !selector.includesNonPlayers()) {
			Text message = TextArgumentType.getTextArgument(context, "message");
			MessageHooks.INSTANCE.onTellRaw(context.getSource().getPlayer(), message);
		}
	}
}
