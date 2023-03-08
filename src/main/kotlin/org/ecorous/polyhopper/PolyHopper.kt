package org.ecorous.polyhopper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.minecraft.server.MinecraftServer
import org.ecorous.polyhopper.config.Config
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlinx.coroutines.runBlocking

object PolyHopper : ModInitializer, CoroutineScope {
    const val MODID: String = "polyhopper"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("PolyHopper")
    var server: MinecraftServer? = null
    val CONFIG : Config = QuiltConfig.create(MODID, "config", Config::class.java)

    override fun onInitialize(mod: ModContainer) {
        ServerLifecycleEvents.READY.register {
            server = it
            runBlocking {
                HopperBot.init()
            }
            launch {
                HopperBot.bot.start()
            }
            MessageHooks.onServerStarted()
        }

        ServerLifecycleEvents.STOPPING.register {
            server = null
            MessageHooks.onServerShutdown()
            runBlocking {
                HopperBot.bot.stop()
            }
        }
    }
    override val coroutineContext = Dispatchers.Default
}
