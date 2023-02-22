package org.sadgames.engine.render.gl.material.textures

import com.kgl.opengl.*

class PSSMTexture(width: Int, height: Int): DepthTexture(width, height) {

    override val textureType; get() = GL_TEXTURE_2D_ARRAY
    override fun loadTexture(bitmap: BitmapWrapper?) = glTexStorage3D(textureType, 1, GL_DEPTH_COMPONENT32, width, height, 3)

    override fun attach() {
        glFramebufferTexture(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, textureId, 0)
        glDrawBuffer(GL_NONE)
    }
}