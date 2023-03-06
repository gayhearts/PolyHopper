package org.ecorous.polyhopper.extensions

import com.kotlindiscord.kord.extensions.commands.converters.impl.string
import com.kotlindiscord.kord.extensions.commands.Arguments
import com.kotlindiscord.kord.extensions.extensions.Extension
import com.kotlindiscord.kord.extensions.extensions.publicSlashCommand
import com.kotlindiscord.kord.extensions.types.respond
import com.kotlindiscord.kord.extensions.types.respondEphemeral
import com.mojang.brigadier.context.CommandContext
import dev.kord.common.Color
import dev.kord.common.annotation.KordPreview
import dev.kord.common.entity.Permission
import dev.kord.common.entity.Snowflake
import dev.kord.rest.builder.message.create.embed
import net.minecraft.server.command.ServerCommandSource
import org.ecorous.polyhopper.PolyHopper

@OptIn(KordPreview::class)
class MainExtension : Extension() {
    override val name: String = "main"
    override suspend fun setup() {

        publicSlashCommand(::RunArgs) {
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
                    val source: ServerCommandSource = PolyHopper.server!!.commandSource
                    PolyHopper.server!!.commandManager.executePrefixedCommand(source, arguments.command)

                }
            }
        }

        publicSlashCommand {
            guild(Snowflake(PolyHopper.CONFIG.bot.guildId))
            name = "stop"
            description = "Stops the server."
            requirePermission(Permission.Administrator)
            action {  }
        }
    }

    inner class RunArgs : Arguments() {
        val command by string {
            name = "command"
            description = "Command to run."
        }
    }
}
