package org.ecorous.polyhopper

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.modules.extra.pluralkit.extPluralKit
import com.kotlindiscord.kord.extensions.utils.ensureWebhook
import dev.kord.common.entity.PresenceStatus
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.execute
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.embed
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import net.minecraft.text.Text
import org.ecorous.polyhopper.config.MessageMode
import org.ecorous.polyhopper.extensions.MainExtension

object HopperBot : CoroutineScope {

    lateinit var bot: ExtensibleBot

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
        if (isPlayer) PolyHopper.LOGGER.debug(Utils.getPlayerAvatarUrl(uuid, username))
        return if (isPlayer) Utils.getPlayerAvatarUrl(uuid, username) else PolyHopper.CONFIG.webhook.serverAvatarUrl
    }

    fun sendMinecraftMessage(displayName: String, uuid: String, username: String, text: Text)
    {
        sendMessage(Utils.minecraftTextToDiscordMessage(text), username, uuid, displayName)
    }

    fun sendEmbed(username: String = "Server", body: EmbedBuilder.() -> Unit) {
        launch {
            if (PolyHopper.CONFIG.bot.messageMode == MessageMode.MESSAGE) {
                bot.kordRef.getChannelOf<MessageChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))?.createEmbed(body)
            } else if (PolyHopper.CONFIG.bot.messageMode == MessageMode.WEBHOOK) {
                var webhook = bot.kordRef.getChannelOf<TextChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))
                    ?.let { ensureWebhook(it, Utils.getWebhookUsername("Server", "Server")) }
                webhook!!.token?.let {
                    webhook.execute(it) {
                        this.avatarUrl = getAvatarUrl(false)
                        if (username != "") this.username = Utils.getWebhookUsername(username, username)
                        embed(body)
                    }
                }
            }
        }
    }
    fun sendMessage(message: String, username: String = "", uuid: String = "", displayName: String = "", avatarUrl: String = "") {
        launch {
            for (item in PolyHopper.CONFIG.bot.minecraftProxyBlacklist) {
                if (item.isNotEmpty() && message.startsWith(item)) {
                    return@launch
                }
            }
            if (PolyHopper.CONFIG.bot.messageMode == MessageMode.MESSAGE) {
                bot.kordRef.getChannelOf<MessageChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))?.createMessage(Utils.getMessageModeMessage(username, displayName, message))
            } else if (PolyHopper.CONFIG.bot.messageMode == MessageMode.WEBHOOK) {
                var webhook = bot.kordRef.getChannelOf<TextChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))
                    ?.let { ensureWebhook(it, Utils.getWebhookUsername("Server", "Server")) }
                webhook!!.token?.let {
                    webhook.execute(it) {
                        this.avatarUrl =
                            getAvatarUrl(((username != "" || displayName != "") && uuid != ""), uuid, username)
                        if (username != "" || displayName != "") this.username =
                            Utils.getWebhookUsername(displayName, username)
                        content = message
                    }
                }
            }
        }
    }
    override val coroutineContext = Dispatchers.Default
}
