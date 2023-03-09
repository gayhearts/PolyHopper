package org.ecorous.polyhopper.config

import org.quiltmc.config.api.Config.Section
import org.quiltmc.config.api.WrappedConfig
import org.quiltmc.config.api.annotations.Comment

class Config : WrappedConfig() {

    val bot : BotSection = BotSection()
    val webhook : WebhookSection = WebhookSection()
    val message : MessageSection = MessageSection()

    inner class BotSection : Section {
        @Comment("The bot token.")
        val token: String = ""

        @Comment("The channel to limit commands to and where messages are sent.")
        val channelId: String = ""

        @Comment("The guild that the bot is to be used in.")
        val guildId: String = ""

        @Comment("The format that discord messages use in-game")
        val ingameFormat: String = "[PolyHopper] <{username}> {message}"

        @Comment("The channel where whitelist are logged")
        val whitelistChannelId: String = ""

        @Comment("Toggle whitelist command")
        val whitelistCommand: Boolean = false

        @Comment("How to send messages, can be either:")
        @Comment("  MESSAGE - Simple discord messages.")
        @Comment("  WEBHOOK - Message via webhook with custom name and avatar.")
        val messageMode : MessageMode = MessageMode.WEBHOOK

        @Comment("Announce deaths?")
        val announceDeaths = true

        @Comment("Announce advancements?")
        val announceAdvancements = true

        @Comment("Announce player join/leave?")
        val announcePlayerJoinLeave = true
    }

    inner class WebhookSection : Section {
        @Comment("The name used for webhook messages, can substitute in {username} or {displayName}")
        val nameFormat: String = "{displayName} @ PolyHopper"

        @Comment("The url for player webhook image, can substitute in {uuid} or {username}.")
        val playerAvatarUrl: String = "https://crafatar.com/renders/head/{uuid}"

        @Comment("The url for server webhook image, can substitute in {uuid} or {username}.")
        val serverAvatarUrl: String = "https://cdn.ecorous.org/blackhole.png"
    }

    inner class MessageSection : Section {
        @Comment("Format for player messages to take, can substitute in {username}, {displayName}, or {text}")
        val messageFormat: String = "<{displayName}> {text}"
    }
}
