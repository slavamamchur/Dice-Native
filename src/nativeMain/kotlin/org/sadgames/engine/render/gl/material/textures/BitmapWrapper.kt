package org.sadgames.engine.render.gl.material.textures

import com.kgl.stb.Channels
import com.kgl.stb.STBImage
import com.kgl.stb.STBInfo
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.objcPtr
import okio.use
import org.sadgames.engine.utils.STBIOFile
import org.sadgames.engine.utils.ptr

@OptIn(DangerousInternalIoApi::class)
class BitmapWrapper(var rawData: Buffer?, val width: Int, val height: Int, val isCompressed: Boolean = false) {

    companion object {
        @OptIn(ExperimentalUnsignedTypes::class)
        fun createColorBitmap(color: UInt): Buffer {
            val size = 4L * Int.SIZE_BYTES
            val buffer = Buffer(Memory(ByteArray(size.toInt()).ptr, size))
            buffer.writeFully(UIntArray(4) { color })
            buffer.reset()

            return buffer
        }

        fun loadFromResource(id: String) = STBIOFile(id).use {
                STBImage.setFlipVerticallyOnLoad(true)
                Pair(STBImage.load(it, Channels.RGB_ALPHA), STBImage.loadInfo(it))
        }

        //todo: loadFromDB inside loadFromResource
    }

    val imageSizeBytes; get() = rawData?.memory?.size ?: 0L
    var name = ""

    constructor(color: UInt): this(createColorBitmap(color), 2, 2) {
        name = "Color bitmap: #$color"
    }

    private constructor(image: Pair<STBImage, STBInfo>): this(Buffer(image.first.buffer), image.second.width, image.second.height) {
        image.first.close()     //???
    }

    constructor(resourceId: String): this(loadFromResource(resourceId)) {
        name = "bitmap from resource: \"$resourceId\" "
    }

    fun release() {
        rawData?.reset()
        rawData?.discardExact(rawData!!.capacity)
        rawData = null
    }
}
