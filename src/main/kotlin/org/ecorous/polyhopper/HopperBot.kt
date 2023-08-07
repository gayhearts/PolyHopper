package org.ecorous.polyhopper

import com.google.gson.Gson
import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.modules.extra.pluralkit.extPluralKit
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.rest.builder.message.EmbedBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.minecraft.text.Text
import org.ecorous.polyhopper.config.MessageMode
import org.ecorous.polyhopper.extensions.MainExtension

object HopperBot : CoroutineScope {

    lateinit var bot: ExtensibleBot
    val gson: Gson = Gson()

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

    }

    fun onPlayerCountChange() {
        runBlocking {
            bot.kordRef.editPresence {
                playing("Minecraft with ${Utils.getPlayerCount()} players!")
            }
        }
    }

    fun getAvatarUrl(isPlayer: Boolean, uuid: String = "", username: String = ""): String {
        if (isPlayer) PolyHopper.LOGGER.info(Utils.getPlayerAvatarUrl(uuid, username))
        return if (isPlayer) Utils.getPlayerAvatarUrl(uuid, username) else PolyHopper.CONFIG.webhook.serverAvatarUrl
    }

    fun sendMinecraftMessage(displayName: String, uuid: String, username: String, text: Text) {
        sendMessage(Utils.minecraftTextToDiscordMessage(text), username, uuid, displayName)
    }

    fun sendEmbed(username: String = "Server", body: EmbedBuilder.() -> Unit) {
        launch {
            if (PolyHopper.CONFIG.bot.messageMode == MessageMode.MESSAGE) {
                bot.kordRef.getChannelOf<MessageChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))?.createEmbed(body)
            } else if (PolyHopper.CONFIG.bot.messageMode == MessageMode.WEBHOOK) {
                val webhookClient = HttpClient(CIO)
                val embed = EmbedBuilder().apply(body)
                val request = webhookClient.request(PolyHopper.CONFIG.webhook.webhookUrl) {
                    method = HttpMethod.Post
                    contentType(ContentType.Application.Json)
                    setBody(
                        """
                        {
                            "content": "",
                            "avatar_url": "${getAvatarUrl(false)}",
                            "embeds": [
                                {
                                    "title": "${if (embed.title == null) "" else embed.title}",
                                    "description": "${if (embed.description == null) "" else embed.description}",
                                    "url": "${if (embed.url == null) "" else embed.url}",
                                    "timestamp": "${if (embed.timestamp == null) "" else embed.timestamp}",
                                    "color": ${if (embed.color == null) "\"\"" else embed.color.hashCode()},
                                    "image": "${if (embed.image == null) "" else embed.image}",
                                    "footer": "${if (embed.footer == null) "" else embed.footer!!.text}",
                                    "thumbnail": "${if (embed.thumbnail == null) "" else embed.thumbnail!!.url}",
                                    "fields": ${embed.fields}
                                }
                            ],
                            "username": "${Utils.getWebhookUsername("Server", "Server")}"
                        }
                    """
                    )
                }
                PolyHopper.LOGGER.info("embed: ${request.status}")
                PolyHopper.LOGGER.info(
                    """
                    {
                        "title": "${if (embed.title == null) "" else embed.title}",
                        "description": "${if (embed.description == null) "" else embed.description}",
                        "url": "${if (embed.url == null) "" else embed.url}",
                        "timestamp": "${if (embed.timestamp == null) "" else embed.timestamp}",
                        "color": ${if (embed.color == null) "\"\"" else embed.color.hashCode()},
                        "image": "${if (embed.image == null) "" else embed.image}",
                        "footer": "${if (embed.footer == null) "" else embed.footer!!.text}",
                        "thumbnail": "${if (embed.thumbnail == null) "" else embed.thumbnail!!.url}",
                        "fields": ${embed.fields}
                    }
                """.trimIndent()
                )
                PolyHopper.LOGGER.info("""
                        {
                            "content": "",
                            "avatar_url": "${getAvatarUrl(false)}",
                            "embeds": [
                                {
                                    "title": "${if (embed.title == null) "" else embed.title}",
                                    "description": "${if (embed.description == null) "" else embed.description}",
                                    "url": "${if (embed.url == null) "" else embed.url}",
                                    "timestamp": "${if (embed.timestamp == null) "" else embed.timestamp}",
                                    "color": ${if (embed.color == null) "\"\"" else embed.color.hashCode()},
                                    "image": "${if (embed.image == null) "" else embed.image}",
                                    "footer": "${if (embed.footer == null) "" else embed.footer!!.text}",
                                    "thumbnail": "${if (embed.thumbnail == null) "" else embed.thumbnail!!.url}",
                                    "fields": ${embed.fields}
                                }
                            ],
                            "username": "${Utils.getWebhookUsername("Server", "Server")}"
                        }
                    """)
            }
        }
    }

    fun sendMessage(
        message: String,
        username: String = "",
        uuid: String = "",
        displayName: String = "",
        avatarUrl: String = ""
    ) {
        launch {
            for (item in PolyHopper.CONFIG.bot.minecraftProxyBlacklist.stream().toList()) {
                if (message.startsWith(item)) {
                    return@launch
                }
            }
            if (PolyHopper.CONFIG.bot.messageMode == MessageMode.MESSAGE) {
                bot.kordRef.getChannelOf<MessageChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))
                    ?.createMessage(Utils.getMessageModeMessage(username, displayName, message))
            } else if (PolyHopper.CONFIG.bot.messageMode == MessageMode.WEBHOOK) {
                val webhookClient = HttpClient(CIO)
                PolyHopper.LOGGER.info((((username != "" || displayName != "") && uuid != "").toString()))
                val request = webhookClient.request(PolyHopper.CONFIG.webhook.webhookUrl) {
                    method = HttpMethod.Post
                    contentType(ContentType.Application.Json)
                    setBody(
                        """
                        {
                            "avatar_url": "${
                            getAvatarUrl(
                                ((username != "" || displayName != "") && uuid != ""),
                                uuid,
                                username
                            )
                        }",
                            "content": "$message",
                            "username": "${Utils.getWebhookUsername(if (displayName == "") "Server" else displayName, if (username == "") "Server" else username)}"
                        }
                    """
                    )
                }
                PolyHopper.LOGGER.info("sendMessage: ${request.status}")
            }
        }
    }

    override val coroutineContext = Dispatchers.Default
}
