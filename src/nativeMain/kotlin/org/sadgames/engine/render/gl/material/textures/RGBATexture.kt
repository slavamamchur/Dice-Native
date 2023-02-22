package org.sadgames.engine.render.gl.material.textures

import com.kgl.opengl.*

open class RGBATexture(
            width: Int,
            height: Int,
            private val attachmentNum: UInt = 0u,
            isMultiSampled: Boolean = false): AbstractFBOTexture(width, height, isMultiSampled) {

    override val textureType; get() = if (isMultiSampled) GL_TEXTURE_2D_MULTISAMPLE else GL_TEXTURE_2D
    protected open val textureFormat; get() = GL_RGBA

    @Throws(UnsupportedOperationException::class) override fun loadTexture(bitmap: BitmapWrapper?) {
        if (isMultiSampled)
            glTexImage2DMultisample(textureType, 4, textureFormat, width, height,true)
        else
            glTexImage2D(textureType, 0, textureFormat.toInt(), width, height,0, textureFormat, GL_UNSIGNED_BYTE,null)
    }

    override fun attach() = glFramebufferTexture2D(GL_DRAW_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + attachmentNum, textureType, textureId, 0)
}