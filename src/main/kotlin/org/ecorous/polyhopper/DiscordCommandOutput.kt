package org.ecorous.polyhopper

import net.minecraft.server.command.CommandOutput
import net.minecraft.text.Text

class DiscordCommandOutput : CommandOutput {
    override fun sendSystemMessage(message: Text?) {

    }

    override fun shouldReceiveFeedback(): Boolean {
        TODO("Not yet implemented")
    }

    override fun shouldTrackOutput(): Boolean {
        TODO("Not yet implemented")
    }

    override fun shouldBroadcastConsoleToOps(): Boolean {
        TODO("Not yet implemented")
    }
}
