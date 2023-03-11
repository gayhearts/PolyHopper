package org.ecorous.polyhopper

import dev.kord.common.entity.Snowflake
import kotlinx.coroutines.runBlocking
import net.minecraft.text.MutableText
import net.minecraft.text.Style
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import java.lang.StringBuilder
import java.util.*
import java.util.regex.Pattern

object Utils {
    private const val OBFUSCATION_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890"

    fun getWebhookUsername(displayName: String, username: String): String {
        return PolyHopper.CONFIG.webhook.nameFormat
                                                   .replace("{displayName}", displayName)
                                                   .replace("{username}", username)
    }

    fun getPlayerAvatarUrl(uuid: String, username: String): String {
        return PolyHopper.CONFIG.webhook.playerAvatarUrl
                                                        .replace("{uuid}", uuid)
                                                        .replace("{username}", username)
    }

    fun getMessageModeMessage(username: String, displayName: String, content: String): String {
        return PolyHopper.CONFIG.message.messageFormat
                                                      .replace("{username}", username)
                                                      .replace("{displayName}", displayName)
                                                      .replace("{text}", content)
    }


    fun getInGameMessage(message: String, username: String): String {
        return PolyHopper.CONFIG.bot.ingameFormat
            .replace("{username}", username)
            .replace("{message}", message) // replace with `.replace("{message}", discordMessageToMinecraftText(message))` when said method is done

    }



    fun getMaxPlayerCount(): Int {
        return PolyHopper.server!!.maxPlayerCount
    }

    fun getCurrentPlayerCount(): Int {
        return PolyHopper.server!!.currentPlayerCount
    }

    fun getPlayerCount(): String {
        return "${PolyHopper.server!!.currentPlayerCount}/${PolyHopper.server!!.maxPlayerCount}"
    }

    fun discordMessageToMinecraftText(message: String) : Text {
        //TODO()
        var result: Text = Text.of(message)
        Text.of("")
        runBlocking {
            TODO(

            )
            var uMPIndex = 0
            var cMPIndex = 0
            var rMPIndex = 0
            var eMPIndex = 0
            var message_result = message
            val userMentionPattern = """(<@!?([0-9]{16,20})>)""".toRegex()
            val channelMentionPattern = """(<#([0-9]{16,20})>)""".toRegex()
            val roleMentionPattern = """(<@&([0-9]{16,20})>)""".toRegex()
            val emojiMentionPattern = """(<a?:([a-zA-Z]{2,32}):[0-9]{16,20}>)""".toRegex()

            for (match in userMentionPattern.findAll(message)) {

                var value = match.value
                val id = Snowflake(value.replace("<@", "").replace(">", ""))
                val username = "@" + HopperBot.bot.kordRef.getGuildOrThrow(Snowflake(PolyHopper.CONFIG.bot.guildId)).getMember(id).displayName

                var usernameText = Text.literal(username).formatted(Formatting.GOLD) as MutableText
                message_result = message_result.replace(match.value, "")
            }
        }
        return result
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

    fun obfuscatedMessage(length: Int) : String {
        val random = Random()
        var rv = ""
        for (i in 0..length) {
            rv += OBFUSCATION_CHARACTERS[random.nextInt(OBFUSCATION_CHARACTERS.length)]
        }
        return rv
    }
}
