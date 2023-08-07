package org.ecorous.polyhopper

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.ensureWebhook
import dev.kord.common.entity.Snowflake
import dev.kord.core.behavior.channel.createEmbed
import dev.kord.core.behavior.execute
import dev.kord.core.entity.channel.MessageChannel
import dev.kord.core.entity.channel.TextChannel
import dev.kord.rest.builder.message.EmbedBuilder
import dev.kord.rest.builder.message.create.WebhookMessageCreateBuilder
import dev.kord.rest.builder.message.create.embed
import kotlinx.coroutines.launch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import java.lang.IllegalStateException

sealed class DiscordMessageSender(val bot: ExtensibleBot, val channelId: Snowflake, val threadId: Snowflake?) : CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    abstract fun sendEmbed(username: String = "Server", body: EmbedBuilder.() -> Unit)

    protected abstract fun sendMessageInternal(message: String, username: String, uuid: String, displayName: String, avatarUrl: String)

    fun sendMessage(message: String, username: String = "", uuid: String = "", displayName: String = "", avatarUrl: String = "") {
        if (passesProxyBlacklist(message)) {
            sendMessageInternal(message, username, uuid, displayName, avatarUrl)
        }
    }

    private fun passesProxyBlacklist(message: String) : Boolean {
        return !PolyHopper.CONFIG.bot.minecraftProxyBlacklist.any { it.isNotEmpty() && message.startsWith(it) }
    }

    protected fun getAvatarUrl(isPlayer: Boolean, uuid: String = "", username: String = ""): String {
        if (isPlayer) PolyHopper.LOGGER.debug(Utils.getPlayerAvatarUrl(uuid, username))
        return if (isPlayer) Utils.getPlayerAvatarUrl(uuid, username) else PolyHopper.CONFIG.webhook.serverAvatarUrl
    }

    class MessageSender(bot: ExtensibleBot, channelId: Snowflake, threadId: Snowflake?) : DiscordMessageSender(bot, channelId, threadId) {
        override fun sendEmbed(username: String, body: EmbedBuilder.() -> Unit) {
            launch {
                getChannel()?.createEmbed(body)
            }
        }

        override fun sendMessageInternal(message: String, username: String, uuid: String, displayName: String, avatarUrl: String) {
            launch {
                getChannel()?.createMessage(Utils.getMessageModeMessage(username, displayName, message))
            }
        }

        private suspend fun getChannel() : MessageChannel? {
            return bot.kordRef.getChannelOf<MessageChannel>(threadId ?: channelId)
        }
    }

    class WebhookSender(bot: ExtensibleBot, channelId: Snowflake, threadId: Snowflake?) : DiscordMessageSender(bot, channelId, threadId) {
        override fun sendEmbed(username: String, body: EmbedBuilder.() -> Unit) {
            launch {
                usingWebhook {
                    this.avatarUrl = getAvatarUrl(false)
                    if (username != "") this.username = Utils.getWebhookUsername(username, username)
                    embed(body)
                }
            }
        }

        override fun sendMessageInternal(message: String, username: String, uuid: String, displayName: String, avatarUrl: String) {
            launch {
                usingWebhook {
                    this.avatarUrl = getAvatarUrl(((username != "" || displayName != "") && uuid != ""), uuid, username)
                    if (username != "" || displayName != "") this.username = Utils.getWebhookUsername(displayName, username)
                    content = message
                }
            }
        }

        private suspend fun usingWebhook(block: WebhookMessageCreateBuilder.() -> Unit) {
            val webhook = bot.kordRef.getChannelOf<TextChannel>(channelId)?.let { ensureWebhook(it, Utils.getWebhookUsername("Server", "Server")) }
                ?: throw IllegalStateException("Failed to find channel with id: $channelId")

            webhook.execute(webhook.token!!, threadId, builder = block)
        }
    }
}
