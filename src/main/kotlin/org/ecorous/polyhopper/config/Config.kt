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

        @Comment("The channel where whitelist are logged")
        val whitelistChannelId: String = ""

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
        @Comment("Url to send messages to, should be in the same channel as bot section above.")
        val webhookUrl: String = ""

        @Comment("The name used for webhook messages.")
        val nameFormat: String = "{username} @ PolyHopper"

        @Comment("The url for webhook image, can substitute in {uuid} or {username}.")
        val avatarUrl: String = "https://crafatar.com/renders/head/{uuid}"
    }

    inner class MessageSection : Section {
        @Comment("Format for player messages to take.")
        val messageFormat: String = "<{username}> {text}"
    }
}
