package org.sadgames.engine.render.gl.material.textures

import com.kgl.opengl.*
import copengl.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT
import copengl.GL_TEXTURE_MAX_ANISOTROPY_EXT
import copengl.glGetFloatv
import copengl.glTexParameterf
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.refTo
import org.sadgames.engine.cache.AbstractEntityCacheManager.CachedEntity
import org.sadgames.engine.utils.isNumber

abstract class AbstractTexture(width: Int,
                               height: Int,
                               bitmap: BitmapWrapper?,
                               textureName: String?,
                               protected var textureParams: TextureParams,
                               protected val isMultiSampled: Boolean = false): CachedEntity {
    companion object {
        const val GL_EXT_TEXTURE_FILTER_ANISOTROPIC = "GL_EXT_texture_filter_anisotropic"
        const val GLOBAL_USE_MIP_MAP = true
    }

    var width = width; private set
    var height = height; private set
    var textureId: UInt = 0u
    var textureSize = bitmap?.imageSizeBytes ?: 0L; protected set
    var textureName = textureName; protected set

    protected var textureData: BitmapWrapper? = null
    protected open val textureType; get() = GL_TEXTURE_2D

    override val name; get() = textureName!!
    override val size; get() = textureSize
    override val isDeleted; get() = textureId == 0u
    override val isReleased; get() = textureData == null

    constructor(width: Int, height: Int, bitmap: BitmapWrapper?, textureName: String? = bitmap?.name, useMipMap: Boolean = GLOBAL_USE_MIP_MAP): this(width, height, bitmap, textureName, TextureParams(useMipMap))
    constructor(bitmap: BitmapWrapper?): this(bitmap?.width ?: 0, bitmap?.height ?: 0, bitmap)

    init {
        createTexture(bitmap)
    }

    @Throws(UnsupportedOperationException::class) protected abstract fun loadTexture(bitmap: BitmapWrapper?)

    protected open fun setTextureParams() {
        glEnable(GL_BLEND)
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA)

        glTexParameteri(textureType, GL_TEXTURE_MIN_FILTER, textureParams.filterMode.gLEnum)
        glTexParameteri(textureType, GL_TEXTURE_MAG_FILTER, textureParams.filterMode.gLEnum)

        glTexParameteri(textureType, GL_TEXTURE_WRAP_S, textureParams.wrapMode.gLEnum)
        glTexParameteri(textureType, GL_TEXTURE_WRAP_T, textureParams.wrapMode.gLEnum)

        if (textureParams.filterMode.isMipMap) {
            val max = FloatArray(16)
            glGetFloatv(GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT, max.refTo(0))
            glTexParameterf(
                textureType,
                GL_TEXTURE_MAX_ANISOTROPY_EXT,
                max[0].coerceAtMost(16f)
            )
        }
    }

    fun createTexture(bitmap: BitmapWrapper?) {
        textureId = glGenTexture()

        if (textureId != 0u) {
            /*if (this is CubeMapTexture)
                glBindTextureCube(textureIds[0])
            else*/ glBindTexture(textureType, textureId)
        }

        textureData = bitmap

        if (textureId != 0u) {
            setTextureParams()

            try {
                loadTexture(bitmap)
            } catch (exception: UnsupportedOperationException) {
                textureId = 0u
            }
        }
    }

    open fun bind(glTextureSlot: UInt = 0u): Int = if (textureId == 0u)
            -1
        else {
            glActiveTexture(GL_TEXTURE0 + glTextureSlot)

            /*if (this is CubeMapTexture)
                glBindTextureCube(textureId)
            else*/
            glBindTexture(textureType, textureId)

            glTextureSlot.toInt()
    }

    fun deleteTexture() {
        glDeleteTexture(textureId)
        textureId = 0u
    }

    @OptIn(DangerousInternalIoApi::class)
    fun loadTextureInternal(target: UInt, bitmap: BitmapWrapper) {
        glPixelStorei(GL_UNPACK_ALIGNMENT, 1)

        glTexImage2D(target,
                    0,
                    GL_RGBA.toInt(),
                    width,
                    height,
                    0,
                    GL_RGBA,
                    GL_UNSIGNED_BYTE,
                    bitmap.rawData?.pointer)
    }

    override fun reloadData() = createTexture(null)
    override fun delete() { if (textureId > 0u) deleteTexture() }
    override fun release() { textureData?.release(); textureData = null }

    /** public static boolean isETC1Supported() {
     * int[] results = new int[20];
     * glGetIntegerv(GL_NUM_COMPRESSED_TEXTURE_FORMATS, results, 0);
     * int numFormats = results[0];
     * if (numFormats > results.length) {
     * results = new int[numFormats];
     * }
     * glGetIntegerv(GL_COMPRESSED_TEXTURE_FORMATS, results, 0);
     * for (int i = 0; i < numFormats; i++) {
     * if (results[i] == ETC1_RGB8_OES) {
     * return true;
     * }
     * }
     * return false;
     * }  */

}