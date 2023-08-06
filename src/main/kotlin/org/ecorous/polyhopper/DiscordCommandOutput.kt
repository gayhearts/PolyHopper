package org.ecorous.polyhopper

import net.minecraft.server.command.CommandOutput
import net.minecraft.text.Text

class DiscordCommandOutput : CommandOutput {
    override fun sendSystemMessage(message: Text?) {
        message?.let { Utils.minecraftTextToDiscordMessage(it) }?.let { HopperBot.sendMessage(it, "Command Output", "", "Command Output") }
    }

    override fun shouldReceiveFeedback(): Boolean {
        return true
    }

    override fun shouldTrackOutput(): Boolean {
        return true
    }

    override fun shouldBroadcastConsoleToOps(): Boolean {
        return true
    }
}
