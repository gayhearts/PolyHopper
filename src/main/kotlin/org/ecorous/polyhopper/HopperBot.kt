package org.ecorous.polyhopper

import com.kotlindiscord.kord.extensions.ExtensibleBot
import com.kotlindiscord.kord.extensions.utils.ensureWebhook
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
import net.minecraft.text.Text
import org.ecorous.polyhopper.config.MessageMode
import org.ecorous.polyhopper.extensions.MainExtension
import org.quiltmc.qkl.library.brigadier.argument.message

object HopperBot : CoroutineScope {

    lateinit var bot: ExtensibleBot

    suspend fun init() {
        val token = PolyHopper.CONFIG.bot.token
        bot = ExtensibleBot(token) {
            extensions {
                add(::MainExtension)
            }
        }
    }

    fun getAvatarUrl(isPlayer: Boolean, uuid: String = "", username: String = ""): String {
        return if (isPlayer) Utils.getPlayerAvatarUrl(uuid, username) else PolyHopper.CONFIG.webhook.serverAvatarUrl
    }

    fun sendMinecraftMessage(displayName: String, uuid: String, username: String, text: Text)
    {
        sendMessage(MessageHooks.minecraftTextToDiscordMessage(text), username, uuid, displayName)
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
            if (PolyHopper.CONFIG.bot.messageMode == MessageMode.MESSAGE) {
                bot.kordRef.getChannelOf<MessageChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))?.createMessage(Utils.getMessageModeMessage(username, displayName, message))
            } else if (PolyHopper.CONFIG.bot.messageMode == MessageMode.WEBHOOK) {
                var webhook = bot.kordRef.getChannelOf<TextChannel>(Snowflake(PolyHopper.CONFIG.bot.channelId))
                    ?.let { ensureWebhook(it, Utils.getWebhookUsername("Server", "Server")) }
                webhook!!.token?.let {
                    webhook.execute(it) {
                        this.avatarUrl = getAvatarUrl((username != "" || displayName != "" || uuid != ""), username, uuid)
                        if (username != "" || displayName != "" || uuid != "") this.username = Utils.getWebhookUsername(displayName, username)
                        content = message
                    }
                }
            }
        }
    }
    override val coroutineContext = Dispatchers.Default
}
