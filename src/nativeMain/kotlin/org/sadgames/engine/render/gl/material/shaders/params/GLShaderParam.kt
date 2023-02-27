package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.refTo
import kotlinx.cinterop.toCPointer
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
                    GLParamType.FLOAT_ATTRIB_ARRAY_PARAM ->
                        if (value is VBOData)
                            setParamValue(value)
                        else
                            throw IllegalStateException("Unexpected value: $paramType")

                    FLOAT_UNIFORM_VECTOR_PARAM, FLOAT_UNIFORM_VECTOR4_PARAM, FLOAT_UNIFORM_MATRIX_PARAM ->
                        if (value is Vector3f)
                            setParamValue(value.toArray())
                        else
                            setParamValue(value as FloatArray)

                    FLOAT_UNIFORM_PARAM -> setParamValue(value as Float)

                    INTEGER_UNIFORM_PARAM -> setParamValue(value as Int)
                }
        }

    val paramReference =
            if (paramType == FLOAT_ATTRIB_ARRAY_PARAM)
                glGetAttribLocation(programId, paramName)
            else
                glGetUniformLocation(programId, paramName)

    var size = 0; private set
    var stride = 0; private set
    var pos = 0; private set
    var vboPtr = 0u; private set

    protected open fun internalLinkParamValue() {
        glBindBuffer(GL_ARRAY_BUFFER, vboPtr)
        glEnableVertexAttribArray(paramReference.toUInt())
        glVertexAttribPointer(paramReference.toUInt(), size, GL_FLOAT, false, stride, pos.toPtr())
        glBindBuffer(GL_ARRAY_BUFFER, 0u)
    }

    @Throws(IllegalArgumentException::class)
    private fun setParamValue(value: VBOData) {
        require(paramType == FLOAT_ATTRIB_ARRAY_PARAM)

        this.size = value.size
        this.stride = value.stride
        this.pos = value.pos
        this.vboPtr = value.vboPtr

        internalLinkParamValue()
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

    @Throws(IllegalArgumentException::class)
    private fun setParamValue(data: Int) {
        if (paramType == INTEGER_UNIFORM_PARAM)
            glUniform1i(paramReference, data)
        else
            throw IllegalArgumentException()
    }

    private fun setParamValue(data: Float) {
        if (paramType == FLOAT_UNIFORM_PARAM && paramReference >= 0)
            glUniform1f(paramReference, data)
        //else
            //throw IllegalArgumentException()
    }

}