package org.ecorous.polyhopper

import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Style
import net.minecraft.text.Text
import org.ecorous.polyhopper.HopperBot.sendEmbed
import java.lang.StringBuilder
import java.util.Optional
import java.util.Random

// Likely temporary, just somewhere to put partial implementations whilst other things are waiting to be worked on e.g. discord bot.
object MessageHooks {
    private const val OBFUSCATION_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"

    fun onPlayerDeath(player: ServerPlayerEntity, message: Text) {
        if (PolyHopper.CONFIG.bot.announceDeaths) {
            // Example: Player661 fell from a high place
            PolyHopper.LOGGER.info(message.string)
        }
    }

    fun onAdvancement(player: ServerPlayerEntity, message: Text) {
        if (PolyHopper.CONFIG.bot.announceAdvancements) {
            sendEmbed {
                title = message.string
            }
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

    fun onMeCommand(player: ServerPlayerEntity?, message: String) {
        PolyHopper.LOGGER.info((player?.displayName?.string ?: "Server") + " /me'd: \"${message}\"")
    }

    fun onSayCommand(player: ServerPlayerEntity?, message: String) {
        PolyHopper.LOGGER.info((player?.displayName?.string ?: "Server") + " /say'd: \"${message}\"")
    }

    fun onTellRaw(player: ServerPlayerEntity?, message: Text) {
        PolyHopper.LOGGER.info((player?.displayName?.string ?: "Server") + " /tellraw'd: \"${minecraftTextToDiscordMessage(message)}\" (${Text.Serializer.toJson(message)})")
    }

    fun onServerStarted() {
        PolyHopper.LOGGER.info("Server started!")
    }

    fun onServerShutdown() {
        PolyHopper.LOGGER.info("Server shutdown!")
    }

    private fun discordMessageToMinecraftText(message: String) : Text {
        TODO()
    }

    private fun minecraftTextToDiscordMessage(message: Text) : String {
        val builder = StringBuilder()
        message.visit({ style, text ->
            if (style.isUnderlined) builder.append("__")
            if (style.isBold) builder.append("**")
            if (style.isItalic) builder.append("*")
            if (style.isStrikethrough) builder.append("~~")
            if (style.isObfuscated) {
                builder.append(obfuscatedMessage(text.length)) // todo: better way to obfuscate text?
            } else {
                builder.append(text)
            }
            if (style.isStrikethrough) builder.append("~~")
            if (style.isItalic) builder.append("*")
            if (style.isBold) builder.append("**")
            if (style.isUnderlined) builder.append("__")

            return@visit Optional.empty<String>()
        }, Style.EMPTY)

        return builder.toString()
    }

    private fun obfuscatedMessage(length: Int) : String {
        val random = Random()
        var rv = ""
        for (i in 0..length) {
            rv += OBFUSCATION_CHARACTERS[random.nextInt(OBFUSCATION_CHARACTERS.length)]
        }
        return rv
    }
}
