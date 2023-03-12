package org.sadgames.engine.render.gl

import com.kgl.opengl.GL_ONE_MINUS_SRC_ALPHA
import org.sadgames.NO_POST_EFFECTS
import org.sadgames.engine.render.gl.material.textures.AbstractTexture

class PostProcessStep(
    var map: AbstractTexture,
    var blendMap: AbstractTexture? = null,
    var effects: UShort = NO_POST_EFFECTS,
    var params: MutableMap<String, Any>? = null,
    var blendFunc: UInt = GL_ONE_MINUS_SRC_ALPHA)
