package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import org.sadgames.engine.render.GLParamType
import org.sadgames.engine.utils.toPtr

class GLInstancedShaderParam(paramName: String, programId: UInt):
        GLShaderParam(GLParamType.FLOAT_ATTRIB_ARRAY_PARAM, paramName, programId) {

    override fun setParamValue(value: VBOData) {
        glBindBuffer(GL_ARRAY_BUFFER, value.handle)
        glEnableVertexAttribArray(paramReference.toUInt())
        glVertexAttribPointer(paramReference.toUInt(), value.size, GL_FLOAT, false, value.stride, value.pos.toPtr())
        glVertexAttribDivisor(paramReference.toUInt(), 1u)
        glBindBuffer(GL_ARRAY_BUFFER, 0u)
    }

}