package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.convert
import kotlinx.cinterop.nativeHeap

open class VBOData(val type: ElementType = ElementType.INDEX,
                   val size: Int = Short.SIZE_BYTES,
                   val stride: Int = 0,
                   val pos: Int = 0,
                   data: Memory) {

    companion object {
        enum class ElementType { VERTEX, INDEX }

        val types: MutableMap<ElementType, UInt> = hashMapOf(ElementType.VERTEX to GL_ARRAY_BUFFER,
            ElementType.INDEX to GL_ELEMENT_ARRAY_BUFFER)
    }

    var vboPtr = glGenBuffer(); private set

    init {
        writeData(data)
        nativeHeap.free(data)
    }

    protected open fun writeData(data: Memory) {
        val glType = types[type]!!

        glBindBuffer(glType, vboPtr)
        glBufferData(glType, data.size, data.pointer, GL_STATIC_DRAW)
        glBindBuffer(glType, 0u)
    }

    open fun clear() {
        if (vboPtr != 0u) {
            glDeleteBuffer(vboPtr)
            vboPtr = 0u
        }
    }
}