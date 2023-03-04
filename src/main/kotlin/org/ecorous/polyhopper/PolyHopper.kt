package org.ecorous.polyhopper

import org.ecorous.polyhopper.config.Config
import org.quiltmc.loader.api.ModContainer
import org.quiltmc.loader.api.config.QuiltConfig
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object PolyHopper : ModInitializer {
    val MODID: String = "polyhopper"
    @JvmField
    val LOGGER: Logger = LoggerFactory.getLogger("PolyHopper")
    val CONFIG : Config = QuiltConfig.create(MODID, "config", Config::class.java)

    override fun onInitialize(mod: ModContainer) {

    }
}
