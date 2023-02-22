package org.sadgames.engine.render.gl.material.textures

import com.kgl.opengl.*
import kotlinx.cinterop.refTo
import org.sadgames.engine.render.gl.material.textures.TextureParams.TextureWrap

open class DepthTexture(width: Int, height: Int, isMultiSampled: Boolean = false): RGBATexture(width, height, isMultiSampled = isMultiSampled) {

  override val textureType: UInt; get() = if (isMultiSampled) GL_TEXTURE_2D_MULTISAMPLE else GL_TEXTURE_2D

  override fun loadTexture(bitmap: BitmapWrapper?) {
    if (isMultiSampled)
      glTexImage2DMultisample(textureType, 4, GL_DEPTH_COMPONENT32, width, height, true)
    else
      glTexImage2D(textureType, 0, GL_DEPTH_COMPONENT32.toInt(), width, height, 0, GL_DEPTH_COMPONENT, GL_FLOAT, null)
  }

  override fun attach() = glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, textureType, textureId, 0)

  override fun setTextureParams() {
    textureParams.filterMode = TextureParams.TextureFilter.Linear
    textureParams.wrapMode = TextureWrap.CLampToBorder

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, textureParams.filterMode.gLEnum)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, textureParams.filterMode.gLEnum)

    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, textureParams.wrapMode.gLEnum)
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, textureParams.wrapMode.gLEnum)

    glTexParameterfv(GL_TEXTURE_2D, GL_TEXTURE_BORDER_COLOR, floatArrayOf(1.0f, 1.0f, 1.0f, 1.0f).refTo(0))
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_MODE, GL_COMPARE_REF_TO_TEXTURE.toInt())
    glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_COMPARE_FUNC, GL_LEQUAL.toInt())
  }
}
