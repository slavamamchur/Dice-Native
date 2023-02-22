package org.sadgames.engine.render.gl.material.textures

import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*

class BitmapWrapper @OptIn(DangerousInternalIoApi::class) constructor(val rawData: Buffer?, val width: Int, val height: Int, val isCompressed: Boolean = false) {
    @OptIn(DangerousInternalIoApi::class)
    val imageSizeBytes; get() = rawData?.memory?.size ?: 0L
    var name = ""

    @OptIn(DangerousInternalIoApi::class)
    fun release() {
        //todo: rawData?. ???
    }
}
