package org.ecorous.polyhopper

import net.minecraft.text.Text

object Utils {
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
}
