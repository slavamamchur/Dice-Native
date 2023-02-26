package org.sadgames.engine.render.gl.fbo

import com.kgl.opengl.*
import kotlinx.cinterop.refTo
import org.sadgames.engine.GameEngine
import org.sadgames.engine.render.gl.material.textures.AbstractTexture
import org.sadgames.engine.utils.Color4f

@Suppress("LeakingThis")
abstract class AbstractFBO(var width: Int,
                           var height: Int,
                           protected val clearColor: Color4f,
                           protected var hasAdditionalTextures: Boolean = false,
                           attachmentsCnt: Int = 1,
                           protected val isMultiSampled: Boolean = false,
                           protected val isFloat32: Boolean = false) {

    var activeTexture = 0
    val colorAttachments: MutableList<AbstractTexture?> = ArrayList()
    val fboTexture; get() = colorAttachments[activeTexture]
    val blitMask; get() = getBltMask()

    protected val fboID: UInt = glGenFramebuffer()
    protected val colorBuffers: ArrayList<UInt> = ArrayList()

    init {
        glBindFramebuffer(GL_FRAMEBUFFER, fboID)

        for (i in 0 until attachmentsCnt)
            colorAttachments += attachTexture(i)

        //val error = GL11.glGetError()
        //println(error)

        val status = glCheckFramebufferStatus(GL_FRAMEBUFFER)
        if (status != GL_FRAMEBUFFER_COMPLETE) {
            //todo: check error
            val error = glGetError()
            println(error)
            cleanUp()
            throw RuntimeException("GL_FRAMEBUFFER_COMPLETE failed, CANNOT use FBO")
        }

        unbind()
    }

    protected abstract fun attachTexture(num: Int): AbstractTexture?
    protected abstract fun getBltMask(): UInt

    operator fun get(index: Int) = colorAttachments[index]
    infix fun blit(target: AbstractFBO?) {
        if (target != null) resolve2FBO(target) else resolve2Screen(activeTexture)
    }

    operator fun invoke(index: Int): AbstractFBO {
        activeTexture = index
        return this
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    open fun bind() {
        glBindTexture(GL_TEXTURE_2D, 0u)
        glBindFramebuffer(GL_FRAMEBUFFER, fboID)

        val buffers = UIntArray(colorAttachments.size)
        for (i in 0 until colorAttachments.size) {
            buffers[i] = GL_COLOR_ATTACHMENT0 + i.toUInt()
        }

        glDrawBuffers(colorAttachments.size, buffers.refTo(0))
        glViewport(0, 0, width, height)
        glClearColor(clearColor.x, clearColor.y, clearColor.z, clearColor.w)
        glClearDepth(1.0)
        glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)
    }

    fun unbind() = glBindFramebuffer(GL_FRAMEBUFFER, 0u)

    fun resolve2FBO(fbo: AbstractFBO, texture: Int = activeTexture) {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, fbo.fboID)
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fboID)
        glReadBuffer(GL_COLOR_ATTACHMENT0 + texture.toUInt())
        glBlitFramebuffer(0, 0, width, height,
                          0, 0, fbo.width, fbo.height,
                          blitMask,
                          GL_NEAREST)
        unbind()
    }

    fun resolve2Screen(texture: Int) {
        glBindFramebuffer(GL_DRAW_FRAMEBUFFER, 0u)
        glBindFramebuffer(GL_READ_FRAMEBUFFER, fboID)
        glReadBuffer(GL_COLOR_ATTACHMENT0 + texture.toUInt())
        glDrawBuffer(GL_BACK)
        glBlitFramebuffer(0, 0, width, height,
                          0, 0, GameEngine.screenWidth, GameEngine.screenHeight,
                          GL_COLOR_BUFFER_BIT,
                          GL_NEAREST)
        unbind()
    }

    open fun cleanUp() {
        unbind()
        glDeleteFramebuffer(fboID)

        colorAttachments.forEach{ it?.deleteTexture() }
        colorAttachments.clear()
    }
}