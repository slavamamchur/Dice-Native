package org.sadgames.engine.render

import org.sadgames.engine.utils.Color4f

/**
 * Created by Slava Mamchur on 22.02.2023.
 */

val DEPTH_BUFFER_CLEAR_COLOR = Color4f(1.0f, 1.0f, 1.0f, 1.0f)

const val FBO_TEXTURE_SLOT = 6

enum class GLParamType {
    FLOAT_ATTRIB_ARRAY_PARAM,
    FLOAT_UNIFORM_VECTOR_PARAM,
    FLOAT_UNIFORM_VECTOR4_PARAM,
    FLOAT_UNIFORM_MATRIX_PARAM,
    FLOAT_UNIFORM_PARAM,
    INTEGER_UNIFORM_PARAM
}

