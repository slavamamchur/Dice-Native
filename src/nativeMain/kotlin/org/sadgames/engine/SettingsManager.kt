package org.sadgames.engine

import org.sadgames.engine.render.GraphicsQuality

/**
 * Created by Slava Mamchur on 09.03.2023.
 */

@ThreadLocal
object SettingsManager {
    val graphicsQualityLevel by lazy { GraphicsQuality.ULTRA } //todo: from ini file
    var isIn_2D_Mode = false
}