package org.sadgames.engine.render.gl.material.textures

import com.kgl.stb.Channels
import com.kgl.stb.STBImage
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.*
import okio.use
import org.sadgames.engine.utils.STBIOFile
import org.sadgames.engine.utils.clone
import org.sadgames.engine.utils.ptr
import platform.posix.alloca

@OptIn(DangerousInternalIoApi::class)
class BitmapWrapper(var rawData: Memory?, val width: Int, val height: Int, val isCompressed: Boolean = false) {

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun createColorBitmap(color: UInt): Memory {
            val size = 4L * Int.SIZE_BYTES
            val buffer = Buffer(Memory(nativeHeap.allocArray(size), size))
            buffer.writeFullyLittleEndian(UIntArray(4) { color })

            return buffer.memory
        }

        fun loadFromResource(id: String) = STBIOFile(id).use {
                STBImage.setFlipVerticallyOnLoad(true)
                STBImage.load(it, Channels.RGB_ALPHA)
        }

        //todo: loadFromDB inside loadFromResource
    }

    val imageSizeBytes; get() = rawData?.size ?: 0L
    var name = ""

    constructor(color: UInt): this(createColorBitmap(color), 2, 2) {
        name = "Color bitmap: #$color"
    }

    private constructor(image: STBImage): this(image.buffer, image.info.width, image.info.height)

    constructor(resourceId: String): this(loadFromResource(resourceId)) {
        name = "bitmap from resource: \"$resourceId\" "
    }

    fun release() {
        if (rawData != null)
            nativeHeap.free(rawData!!)
    }
}
