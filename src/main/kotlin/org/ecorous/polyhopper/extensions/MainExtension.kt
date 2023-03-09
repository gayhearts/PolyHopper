package org.ecorous.polyhopper.extensions

import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.ephemeralSlashCommand
import com.kotlindiscord.kord.extensions.extensions.event
import com.kotlindiscord.kord.extensions.modules.extra.pluralkit.events.ProxiedMessageCreateEvent
import com.kotlindiscord.kord.extensions.modules.extra.pluralkit.events.UnProxiedMessageCreateEvent
import com.kotlindiscord.kord.extensions.types.respond
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.core.entity.Member
import dev.kord.rest.builder.message.create.embed
import net.minecraft.server.command.ServerCommandSource
import net.minecraft.text.Text
import net.minecraft.util.math.Vec2f
import net.minecraft.util.math.Vec3d
import org.ecorous.polyhopper.DiscordCommandOutput
import org.ecorous.polyhopper.PolyHopper

@OptIn(KordPreview::class)
class MainExtension : Extension() {
    override val name: String = "main"
    private val channelId: Snowflake = Snowflake(PolyHopper.CONFIG.bot.channelId)
    override suspend fun setup() {

        ephemeralSlashCommand(::RunArgs) {
            guild(Snowflake(PolyHopper.CONFIG.bot.guildId))
            name = "run"
            description = "Run a command through the server."
            requirePermission(Permission.Administrator)
            action {
                if (PolyHopper.server == null) {
                    respond {
                        embed {
                            title = "Server might not be started yet! `server` is still null!"
                            color = Color(255, 0, 0)
                        }
                    }
                } else {
                    // todo: something here is broken
                    //   Command Output @ PolyHopper — Today at 22:02
                    //     Unknown or incomplete command, see below for error
                    //     say Hello World<--[HERE]
                    val source = ServerCommandSource(
                        DiscordCommandOutput(),
                        Vec3d.ZERO,
                        Vec2f.ZERO,
                        PolyHopper.server!!.overworld,
                        0,
                        "Discord (${user.asUser().username})",
                        Text.of("Discord (${user.asUser().username})"),
                        PolyHopper.server!!,
                        null
                    )
                    PolyHopper.server!!.commandManager.executePrefixedCommand(source, arguments.command)

                }
            }
        }

        ephemeralSlashCommand {
            guild(Snowflake(PolyHopper.CONFIG.bot.guildId))
            name = "stop"
            description = "Stops the server."
            requirePermission(Permission.Administrator)
            action {
                PolyHopper.server?.stop(false)
                respond {
                    content = "Server stopped."
                }
            }
        }

        // todo: Should definitely clean these up and improve implementation like converting discord message to minecraft text.
        event<ProxiedMessageCreateEvent> {
            action {
                if (event.message.channelId == channelId) {
                    val server = PolyHopper.server!!
                    server.execute {
                        // note: display name doesn't include system tag.
                        server.playerManager.broadcastSystemMessage(Text.literal("PolyHopper - <${event.pkMessage.member?.displayName ?: "???"}> ${event.message.content}"), false)
                    }
                }
            }
        }

        event<UnProxiedMessageCreateEvent> {
            action {
                if (event.message.channelId == channelId) {
                    val author : Member? = event.author
                    if (author != null && !author.isBot) {
                        val server = PolyHopper.server!!
                        server.execute {
                            server.playerManager.broadcastSystemMessage(Text.literal("PolyHopper - <${author.displayName}> ${event.message.content}"), false)
                        }
                    }
                }
            }
        }
    }

    inner class RunArgs : Arguments() {
        val command by string {
            name = "command"
            description = "Command to run."
        }
    }
}
