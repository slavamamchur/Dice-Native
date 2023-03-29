package org.sadgames.engine.render.gl.material.textures

import com.kgl.opengl.glGenerateMipmap
import org.sadgames.engine.render.gl.material.textures.BitmapWrapper.Companion.createColorBitmap
import org.sadgames.engine.render.gl.material.textures.BitmapWrapper.Companion.getBitmapResource
import org.sadgames.engine.render.gl.material.textures.BitmapWrapper.Companion.loadFromImageFile
import org.sadgames.engine.utils.isNumber

class BitmapTexture(bitmap: BitmapWrapper, useMipMap: Boolean = GLOBAL_USE_MIP_MAP):
        AbstractTexture(bitmap.width, bitmap.height, bitmap, bitmap.name, useMipMap) {
    companion object {
        fun loadFrom(resource: String) = BitmapTexture(getBitmapResource(resource))
    }

    @Throws(UnsupportedOperationException::class) override fun loadTexture(bitmap: BitmapWrapper?) {
        if (bitmap != null) {
            loadTextureInternal(textureType, bitmap)

            if (textureParams.filterMode.isMipMap)
                glGenerateMipmap(textureType)
        }
    }

    override fun reloadData() = createTexture(textureData ?: loadFrom(textureName!!).textureData)
}
