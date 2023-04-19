package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import kotlinx.cinterop.refTo
import org.sadgames.engine.render.GLParamType
import org.sadgames.engine.render.GLParamType.*
import org.sadgames.engine.utils.Vector3f
import org.sadgames.engine.utils.toArray
import org.sadgames.engine.utils.toPtr

open class GLShaderParam(protected val paramType: GLParamType, val paramName: String, programId: UInt) {

    var value: Any? = null
        set(value) {
            field = value

            if (paramReference >= 0)
                when (paramType) {
                    FLOAT_ATTRIB_ARRAY_PARAM ->
                        if (value is VBOData)
                            setParamValue(value)
                        else
                            throw IllegalStateException("Unexpected value: $paramType")

                    FLOAT_UNIFORM_VECTOR_PARAM, FLOAT_UNIFORM_VECTOR4_PARAM, FLOAT_UNIFORM_MATRIX_PARAM ->
                            setParamValue(if (value is Vector3f) value.toArray() else value as FloatArray)

                    FLOAT_UNIFORM_PARAM -> setParamValue(value as Float)

                    INTEGER_UNIFORM_PARAM -> setParamValue(value as Int)
                }
        }

    val paramReference =
            if (paramType == FLOAT_ATTRIB_ARRAY_PARAM)
                glGetAttribLocation(programId, paramName)
            else
                glGetUniformLocation(programId, paramName)

    protected open fun setParamValue(value: VBOData) {
        glBindBuffer(GL_ARRAY_BUFFER, value.handle)
        glEnableVertexAttribArray(paramReference.toUInt())
        glVertexAttribPointer(paramReference.toUInt(), value.size, GL_FLOAT, false, value.stride, value.pos.toPtr())
        glBindBuffer(GL_ARRAY_BUFFER, 0u)
    }

    @Throws(IllegalArgumentException::class)
    private fun setParamValue(data: FloatArray) {
        if (paramType == FLOAT_UNIFORM_VECTOR_PARAM && data.size >= 3)
            glUniform3fv(paramReference, 1, data.refTo(0))
        else if (paramType == FLOAT_UNIFORM_VECTOR4_PARAM && data.size >= 4)
            glUniform4fv(paramReference, 1, data.refTo(0))
        else if (paramType == FLOAT_UNIFORM_MATRIX_PARAM && data.size == 16)
            glUniformMatrix4fv(paramReference, 1,false, data.refTo(0))
        else
            throw IllegalArgumentException()
    }

    private inline fun setParamValue(data: Int) = glUniform1i(paramReference, data)

    private inline fun setParamValue(data: Float) = glUniform1f(paramReference, data)

}