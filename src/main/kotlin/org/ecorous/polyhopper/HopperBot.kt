package org.ecorous.polyhopper

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.modules.extra.pluralkit.extPluralKit
import dev.kord.rest.builder.message.EmbedBuilder
import kotlinx.coroutines.runBlocking
import net.minecraft.text.Text
import org.ecorous.polyhopper.extensions.MainExtension

object HopperBot {

    lateinit var bot: ExtensibleBot

    private lateinit var messageSender: DiscordMessageSender

    suspend fun init() {
        val token = PolyHopper.CONFIG.bot.token

        bot = ExtensibleBot(token) {
            extensions {
                extPluralKit()
                add(::MainExtension)
            }
            presence {
                playing("Minecraft with ${Utils.getPlayerCount()} players!")
            }
        }

        messageSender = PolyHopper.CONFIG.bot.messageMode.constructSender(bot)
    }

    fun onPlayerCountChange() {
        runBlocking {
            bot.kordRef.editPresence {
                playing("Minecraft with ${Utils.getPlayerCount()} players!")
            }
        }
    }

    fun sendMinecraftMessage(displayName: String, uuid: String, username: String, text: Text) {
        sendMessage(Utils.minecraftTextToDiscordMessage(text), username, uuid, displayName)
    }

    fun sendEmbed(username: String = "Server", body: EmbedBuilder.() -> Unit) {
        messageSender.sendEmbed(username, body)
    }

    fun sendMessage(message: String, username: String = "", uuid: String = "", displayName: String = "", avatarUrl: String = "") {
        messageSender.sendMessage(message, username, uuid, displayName, avatarUrl)
    }
}
