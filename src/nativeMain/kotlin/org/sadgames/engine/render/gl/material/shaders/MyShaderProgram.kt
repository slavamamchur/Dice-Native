package org.sadgames.engine.render.gl.material.shaders

import com.kgl.opengl.*
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.charsets.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import org.sadgames.engine.utils.ptr
import org.sadgames.engine.utils.readTextFromFile

class MyShaderProgram internal constructor(private val programList: Map<UInt, String>) {

    companion object {
        var prependCode = "#version 410\n#define GLES330\n"
        val programTypes: MutableMap<UInt, String> = HashMap()

        init {
            programTypes[GL_VERTEX_SHADER] = "Vertex"
            programTypes[GL_TESS_CONTROL_SHADER] = "Tessellation Control"
            programTypes[GL_TESS_EVALUATION_SHADER] = "Tessellation Evaluation"
            programTypes[GL_GEOMETRY_SHADER] = "Geometry"
            programTypes[GL_FRAGMENT_SHADER] = "Fragment"
            programTypes[GL_COMPUTE_SHADER] = "Compute"
        }

        /** String defineClipPlane = glExtensions().contains(GL_EXT_clip_cull_distance) ? "#define CLIP_PLANE\n" : "";
        MyShaderProgram.prependVertexCode += defineClipPlane; */
    }

    private val handlesList: MutableList<UInt> = ArrayList()

    var log = ""
        get(): String {
            if (isCompiled && field.isEmpty())
                field = glGetProgramInfoLog(programId)

            return field
        }

    var programId = 0u
    var isCompiled = false

    val attributeNames: MutableList<String> = ArrayList()
    val uniformTypes: MutableMap<String, UInt> = HashMap()

    init {
        require((programList[GL_VERTEX_SHADER]?.isNotEmpty() ?: false
                && programList[GL_FRAGMENT_SHADER]?.isNotEmpty() ?: false)
                || programList[GL_COMPUTE_SHADER]?.isNotEmpty() ?: false) { "Incorrect shader components" }

        compileShaders()

        if(isCompiled) {
            fetchAttributes()
            fetchUniforms()
        }
    }

    private fun loadShader(type: UInt, source: String): UInt {
            var shader = glCreateShader(type)

            if (shader > 0u) {
                val error = IntArray(1)

                glShaderSource(shader, source)
                glCompileShader(shader)
                glGetShaderiv(shader, GL_COMPILE_STATUS, error.ptr)

                if (error[0] == 0) {
                    val sType = programTypes[type]
                    log += "$sType program:\n" + glGetShaderInfoLog(shader)
                    glDeleteShader(shader)
                    shader = 0u
                }
            }

            return shader
    }

    private fun linkProgram(program: UInt): UInt {
        val error = IntArray(1)

        if (program > 0u) {
            handlesList.forEach {
                glAttachShader(program, it)
            }

            glLinkProgram(program)
            glGetProgramiv(program, GL_LINK_STATUS, error.ptr)
        }

        return if (error[0] == 0) 0u else program
    }

    private fun compileShaders() {
        run loop@{
            programList.forEach() {
                //todo: change to relative path for release build
                val handle = loadShader(it.key, prependCode + readTextFromFile("/home/slava/temp/resources/${it.value}"))
                isCompiled = handle > 0u

                if (isCompiled)
                    handlesList.add(handle)
                else
                    return@loop
            }
        }

        programId = if (isCompiled) linkProgram(glCreateProgram()) else 0u
        isCompiled = programId > 0u
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun fetchAttributes() {
        val length = IntArray(1)
        val size = IntArray(1)
        val type = UIntArray(1)
        val name = ByteArray(256)

        glGetProgramiv(programId, GL_ACTIVE_ATTRIBUTES, length.ptr)
        val numAttributes = length[0]
        for (i in 0u until numAttributes.toUInt()) {
            glGetActiveAttrib(programId, i, 256, length.ptr, size.ptr, type.ptr, name.ptr)
            attributeNames.add(String(name, 0, length[0], Charsets.UTF_8))
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    private fun fetchUniforms() {
        val length = IntArray(1)
        val size = IntArray(1)
        val type = UIntArray(1)
        val name = ByteArray(256)

        glGetProgramiv(programId, GL_ACTIVE_UNIFORMS, length.ptr)
        val numUniforms = length[0]
        for (i in 0u until numUniforms.toUInt()) {
            glGetActiveUniform(programId, i, 256, length.ptr, size.ptr, type.ptr, name.ptr)
            uniformTypes[String(name, 0, length[0], Charsets.UTF_8)] = type[0]
        }
    }

    inline fun begin() = glUseProgram(programId)
    inline fun end() = glUseProgram(0u)

    fun run(width: UInt, height: UInt) {
        if(programList[GL_COMPUTE_SHADER]?.isNotEmpty() == true)
            glDispatchCompute(width / 16u, height / 16u, 1u)
    }

    fun dispose() {
        glUseProgram(0u)

        handlesList.forEach {
            glDetachShader(programId, it)
            glDeleteShader(it)
        }

        glDeleteProgram(programId)
    }
}