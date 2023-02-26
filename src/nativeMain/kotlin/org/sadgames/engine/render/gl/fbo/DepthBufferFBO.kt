package org.sadgames.engine.render.gl.fbo

import com.kgl.opengl.GL_DEPTH_BUFFER_BIT
import org.sadgames.engine.render.DEPTH_BUFFER_CLEAR_COLOR
import org.sadgames.engine.render.FBO_TEXTURE_SLOT
import org.sadgames.engine.render.gl.material.textures.DepthTexture
import org.sadgames.engine.utils.Color4f

open class DepthBufferFBO(width: Int, height: Int, color: Color4f = DEPTH_BUFFER_CLEAR_COLOR, isMultiSampled: Boolean = false): AbstractFBO(width, height, color, isMultiSampled = isMultiSampled) {
    override fun attachTexture(num: Int) = DepthTexture(width, height, isMultiSampled)(FBO_TEXTURE_SLOT, true)
    override fun getBltMask() = GL_DEPTH_BUFFER_BIT
}
