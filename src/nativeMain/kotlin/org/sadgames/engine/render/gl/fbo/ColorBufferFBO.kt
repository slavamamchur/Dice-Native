package org.sadgames.engine.render.gl.fbo

import com.kgl.opengl.*
import kotlinx.cinterop.refTo
import org.sadgames.engine.render.FBO_TEXTURE_SLOT
import org.sadgames.engine.render.gl.material.textures.AbstractTexture
import org.sadgames.engine.render.gl.material.textures.DepthTexture
import org.sadgames.engine.render.gl.material.textures.RGBATexture
import org.sadgames.engine.utils.Color4f

open class ColorBufferFBO(width: Int, height: Int, clearColor: Color4f, hasDepthTexture: Boolean = false, attachmentsCnt: Int = 1, isMultiSampled: Boolean = false, isFloat32: Boolean = false):
                AbstractFBO(width, height, clearColor, hasDepthTexture, attachmentsCnt, isMultiSampled, isFloat32) {

    private var depthBuffer: UInt = 0u
    lateinit var depthTexture: AbstractTexture; private set

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun attachTexture(num: Int): AbstractTexture? {
        if (num == 0)
            if (hasAdditionalTextures) {
                depthTexture = DepthTexture(width, height)(0, true)
            } else {
                depthBuffer = glGenRenderbuffer()
                glBindRenderbuffer(GL_RENDERBUFFER, depthBuffer)
                if (!isMultiSampled /*&& !isFloat32*/)
                    glRenderbufferStorage(GL_RENDERBUFFER, GL_DEPTH_COMPONENT16, width, height)
                else
                    glRenderbufferStorageMultisample(GL_RENDERBUFFER, 4, GL_DEPTH_COMPONENT16, width, height)

                glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_RENDERBUFFER, depthBuffer)
            }

        return if (!isMultiSampled) {
                    //(if (!isFloat32)
                        RGBATexture(width, height, num.toUInt())
                    //else
                        //RGBFTexture(width, height, num, true)) (FBO_TEXTURE_SLOT + num, true)
               }
               else {
                    val colorBuffer = UIntArray(1)
                    colorBuffer[0] = glGenRenderbuffer()
                    glBindRenderbuffer(GL_RENDERBUFFER, colorBuffer[0])
                    glRenderbufferStorageMultisample(GL_RENDERBUFFER, 4, GL_RGBA8, width, height)
                    glFramebufferRenderbuffer(GL_FRAMEBUFFER, GL_COLOR_ATTACHMENT0 + num.toUInt(), GL_RENDERBUFFER, colorBuffer[0])
                    colorBuffers.add(colorBuffer[0])

                    null
               }
    }

    override fun getBltMask() = (if (!hasAdditionalTextures) GL_DEPTH_BUFFER_BIT else 0u) or  (if (!isMultiSampled) 0u else GL_COLOR_BUFFER_BIT)

    @OptIn(ExperimentalUnsignedTypes::class)
    override fun cleanUp() {
        if (hasAdditionalTextures)
            depthTexture.deleteTexture()
        else
            glDeleteRenderbuffer(depthBuffer)

        colorBuffers.forEach { glDeleteRenderbuffer(it) }
        colorBuffers.clear()

        super.cleanUp()
    }
}