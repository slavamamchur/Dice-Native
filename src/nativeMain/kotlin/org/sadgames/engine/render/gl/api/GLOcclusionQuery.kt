package org.sadgames.engine.render.gl.api

import com.kgl.opengl.*
import org.sadgames.engine.utils.ptr

class GLOcclusionQuery {

    val id = glGenQuery()
    var isInUse = false; private set
    @OptIn(ExperimentalUnsignedTypes::class)
    val isResultReady: Boolean; get() {
            val buffer = UIntArray(1)
            glGetQueryObjectuiv(id, GL_QUERY_RESULT_AVAILABLE, buffer.ptr)

            return buffer[0] == GL_TRUE
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    val result: Int; get() {
        isInUse = false

        val buffer = UIntArray(1)
        glGetQueryObjectuiv(id, GL_QUERY_RESULT, buffer.ptr)

        return buffer[0].toInt()
    }

    fun start() {
        glBeginQuery(GL_SAMPLES_PASSED, id)
        isInUse = true
    }

    inline fun end() = glEndQuery(GL_SAMPLES_PASSED)
    inline fun delete() = glDeleteQuery(id)

}
