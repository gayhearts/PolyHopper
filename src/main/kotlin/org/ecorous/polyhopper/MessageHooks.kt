package org.ecorous.polyhopper

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Text

// Likely temporary, just somewhere to put partial implementations whilst other things are waiting to be worked on e.g. discord bot.
object MessageHooks {
    fun onPlayerDeath(player: ServerPlayerEntity, message: Text) {
        if (PolyHopper.CONFIG.bot.announceDeaths) {
            // Example: Player661 fell from a high place
            PolyHopper.LOGGER.info(message.string)
        }
    }

    fun onAdvancement(player: ServerPlayerEntity, message: Text) {
        if (PolyHopper.CONFIG.bot.announceAdvancements) {
            // todo: May want to customize this further using fancy embeds.
            //  e.g. adding hover text as a tag line
            // Example: Player661 has completed the challenge [Arbalistic]
            PolyHopper.LOGGER.info(message.string)
        }
    }

    fun onPlayerConnected(player: ServerPlayerEntity) {
        if (PolyHopper.CONFIG.bot.announcePlayerJoinLeave) {
            // Example: Player661 has joined the game.
            PolyHopper.LOGGER.info(player.displayName.string + " has joined the game.")
        }
    }

    fun onPlayerDisconnected(player: ServerPlayerEntity, reason: Text) {
        if (PolyHopper.CONFIG.bot.announcePlayerJoinLeave) {
            // todo: do we want to output the reason too?
            //  This can contain ban messages which otherwise aren't shown.
            // Example: Player661 has left the game.
            PolyHopper.LOGGER.info(player.displayName.string + " has left the game.")
        }
    }

    fun onChatMessageSent(player: ServerPlayerEntity, message: String) {
        // Example: Player56 said: "Hello World!"
        PolyHopper.LOGGER.info(player.displayName.string + " said: \"${message}\"")
    }
}
