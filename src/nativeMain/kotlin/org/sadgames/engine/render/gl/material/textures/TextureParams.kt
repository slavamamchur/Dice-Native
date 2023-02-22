package org.sadgames.engine.render.gl.material.textures

import com.kgl.opengl.*
import org.sadgames.engine.render.gl.material.textures.TextureParams.TextureFilter.Linear
import org.sadgames.engine.render.gl.material.textures.TextureParams.TextureFilter.MipMapLinearLinear

class TextureParams(var filterMode: TextureFilter, var wrapMode: TextureWrap) {
    enum class TextureFilter(val gLEnum: Int) {
        /** Fetch the nearest texel that best maps to the pixel on screen.  */
        Nearest(GL_NEAREST.toInt()),

        /** Fetch four nearest texels that best maps to the pixel on screen.  */
        Linear(GL_LINEAR.toInt()),

        /** @see TextureFilter.MipMapLinearLinear
         */
        MipMap(GL_LINEAR_MIPMAP_LINEAR.toInt()),

        /** Fetch the best fitting image from the mip map chain based on the pixel/texel ratio and then sample the texels with a
         * nearest filter.  */
        MipMapNearestNearest(GL_NEAREST_MIPMAP_NEAREST.toInt()),

        /** Fetch the best fitting image from the mip map chain based on the pixel/texel ratio and then sample the texels with a
         * linear filter.  */
        MipMapLinearNearest(GL_LINEAR_MIPMAP_NEAREST.toInt()),

        /** Fetch the two best fitting images from the mip map chain and then sample the nearest texel from each of the two images,
         * combining them to the final output pixel.  */
        MipMapNearestLinear(GL_NEAREST_MIPMAP_LINEAR.toInt()),

        /** Fetch the two best fitting images from the mip map chain and then sample the four nearest texels from each of the two
         * images, combining them to the final output pixel.  */
        MipMapLinearLinear(GL_LINEAR_MIPMAP_LINEAR.toInt());

        val isMipMap: Boolean; get() = gLEnum != GL_NEAREST.toInt() && gLEnum != GL_LINEAR.toInt()

    }

    enum class TextureWrap(val gLEnum: Int) {
        MirroredRepeat(GL_MIRRORED_REPEAT.toInt()), ClampToEdge(GL_CLAMP_TO_EDGE.toInt()), CLampToBorder(GL_CLAMP_TO_BORDER.toInt()), Repeat(GL_REPEAT.toInt());

    }

    constructor(useMipMap: Boolean): this(if (useMipMap) MipMapLinearLinear else Linear,
        TextureWrap.Repeat
    )
}