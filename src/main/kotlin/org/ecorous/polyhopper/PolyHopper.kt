package org.ecorous.polyhopper

import org.ecorous.polyhopper.config.Config
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.file.Path

object PolyHopper : ModInitializer {
    val LOGGER: Logger = LoggerFactory.getLogger("PolyHopper")

    override fun onInitialize(mod: ModContainer) {
        val config : Config = QuiltConfig.create("polyhopper", "bot", Path.of(""), Config::class.java)
    }
}
