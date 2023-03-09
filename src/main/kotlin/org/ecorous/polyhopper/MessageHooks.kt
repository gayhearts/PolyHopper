package org.ecorous.polyhopper

import dev.kord.common.Color
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.text.Style
import net.minecraft.text.Text
import org.ecorous.polyhopper.HopperBot.sendEmbed
import org.ecorous.polyhopper.HopperBot.sendMinecraftMessage
import java.lang.StringBuilder
import java.util.Optional
import java.util.Random

// Likely temporary, just somewhere to put partial implementations whilst other things are waiting to be worked on e.g. discord bot.
object MessageHooks {
    private const val OBFUSCATION_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"

    fun onPlayerDeath(player: ServerPlayerEntity, message: Text) {
        if (PolyHopper.CONFIG.bot.announceDeaths) {
            sendEmbed {
                title = message.string
                color = Color(255, 0, 0)
            }
            // Example: Player661 fell from a high place
            PolyHopper.LOGGER.info(message.string)
        }
    }

    fun onAdvancement(player: ServerPlayerEntity, message: Text) {
        if (PolyHopper.CONFIG.bot.announceAdvancements) {
            sendEmbed {
                title = message.string
                color = Color(0, 255, 0)
            }
            // todo: May want to customize this further using fancy embeds.
            //  e.g. adding hover text as a tag line
            // Example: Player661 has completed the challenge [Arbalistic]
            PolyHopper.LOGGER.info(message.string)
        }
    }

    fun onPlayerConnected(player: ServerPlayerEntity) {
        if (PolyHopper.CONFIG.bot.announcePlayerJoinLeave) {
            sendEmbed {
                title = player.displayName.string + " has joined the game"
                color = Color(0, 255, 0)
            }
            // Example: Player661 has joined the game.
            PolyHopper.LOGGER.info(player.displayName.string + " has joined the game.")
        }
    }

    fun onPlayerDisconnected(player: ServerPlayerEntity, reason: Text) {
        if (PolyHopper.CONFIG.bot.announcePlayerJoinLeave) {
            sendEmbed {
                title = player.displayName.string + " has left the game"
                color = Color(255, 0, 0)
            }
            // todo: do we want to output the reason too?
            //  This can contain ban messages which otherwise aren't shown.
            // Example: Player661 has left the game.
            PolyHopper.LOGGER.info(player.displayName.string + " has left the game.")
        }
    }

    fun onChatMessageSent(player: ServerPlayerEntity, message: Text) {
        sendMinecraftMessage(player.displayName.string, player.uuidAsString, player.displayName.string, message)
        // Example: Player56 said: "Hello World!"
        PolyHopper.LOGGER.info(player.displayName.string + " said: \"${message}\"")
    }

    fun onMeCommand(player: ServerPlayerEntity?, message: String) {
        sendEmbed {
            title = player?.displayName?.string + " " + message
            color = Color(0, 0, 255)
        }
        PolyHopper.LOGGER.info((player?.displayName?.string ?: "Server") + " /me'd: \"${message}\"")
    }

    fun onSayCommand(player: ServerPlayerEntity?, message: String) {
        sendEmbed {
            title = (player?.displayName?.string ?: "Server") + ": " + message
            color = Color(0, 0, 255)
        }
        PolyHopper.LOGGER.info((player?.displayName?.string ?: "Server") + " /say'd: \"${message}\"")
    }

    fun onTellRaw(player: ServerPlayerEntity?, message: Text) {
        sendEmbed {
            title = minecraftTextToDiscordMessage(message) // we don't want to say who did it!
            color = Color(0, 0, 255)
        }
        PolyHopper.LOGGER.info((player?.displayName?.string ?: "Server") + " /tellraw'd: \"${minecraftTextToDiscordMessage(message)}\" (${Text.Serializer.toJson(message)})")
    }

    fun onServerStarted() {
        sendEmbed {
            title = "Server started!"
            color = Color(0, 255, 0)
        }
        PolyHopper.LOGGER.info("Server started!")
    }

    fun onServerShutdown() {
        sendEmbed {
            title = "Server stopped!"
            color = Color(255, 0, 0)
        }
        PolyHopper.LOGGER.info("Server shutdown!")
    }

    fun discordMessageToMinecraftText(message: String) : Text {
        TODO()
    }

    fun minecraftTextToDiscordMessage(message: Text) : String {
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
