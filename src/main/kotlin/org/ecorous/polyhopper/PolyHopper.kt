package org.ecorous.polyhopper

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import net.minecraft.server.MinecraftServer
import org.ecorous.polyhopper.config.Config
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.quiltmc.qsl.lifecycle.api.event.ServerLifecycleEvents
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlinx.coroutines.launch

object PolyHopper : ModInitializer, CoroutineScope {
    const val MODID: String = "polyhopper"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("PolyHopper")
    var server: MinecraftServer? = null
    val CONFIG : Config = QuiltConfig.create(MODID, "config", Config::class.java)

    override fun onInitialize(mod: ModContainer) {
        ServerLifecycleEvents.READY.register {
            server = it
            launch {
                HopperBot.init()
            }
        }
    }
    override val coroutineContext = Dispatchers.Default
}
