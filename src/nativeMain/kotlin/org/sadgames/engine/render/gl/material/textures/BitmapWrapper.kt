package org.sadgames.engine.render.gl.material.textures

import com.kgl.stb.Channels
import com.kgl.stb.STBImage
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.*
import okio.use
import org.sadgames.engine.utils.STBIOFile
import org.sadgames.engine.utils.isNumber

@OptIn(DangerousInternalIoApi::class)
class BitmapWrapper internal constructor(var rawData: Memory,
                                         var width: Int = 0,
                                         var height: Int = 0,
                                         val isCompressed: Boolean = false) {
    companion object {
        fun getBitmapResource(id: String) = if (id.isNumber)
            createColorBitmap(id.toUInt(16))
        else loadFromImageFile(id)

        @OptIn(ExperimentalUnsignedTypes::class)
        fun createColorBitmap(color: UInt): BitmapWrapper {
            val size = 4L * Int.SIZE_BYTES
            val buffer = Buffer(Memory(nativeHeap.allocArray(size), size))
            buffer.writeFullyLittleEndian(UIntArray(4) { color })

            return BitmapWrapper(buffer.memory, 2, 2).also { it.name = "Color bitmap: #$color"}
        }

        fun loadFromImageFile(id: String) = STBIOFile(id).use { file ->
            STBImage.setFlipVerticallyOnLoad(true)
                val img = STBImage.load(file, Channels.RGB_ALPHA)
                BitmapWrapper(img.buffer, img.info.width, img.info.height).also {
                    it.name = "bitmap from resource: \"$id\" "
                }
        }

        //todo: loadFromDB inside loadFromResource
    }

    val imageSizeBytes; get() = rawData.size
    var name = ""

    inline fun release() = nativeHeap.free(rawData)
}
