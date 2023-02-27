package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*

@OptIn(DangerousInternalIoApi::class)
class VBOData(val type: ElementType = ElementType.INDEX,
                val size: Int = Short.SIZE_BYTES,
                val stride: Int = 0,
                val pos: Int = 0,
                data: Buffer) {

    companion object {
        enum class ElementType { VERTEX, INDEX }

        val sizes: MutableMap<ElementType, Int> = HashMap()
        val types: MutableMap<ElementType, UInt> = HashMap()

        init {
            sizes[ElementType.VERTEX] = Float.SIZE_BYTES
            sizes[ElementType.INDEX] = Short.SIZE_BYTES
            //todo: add long_index

            types[ElementType.VERTEX] = GL_ARRAY_BUFFER
            types[ElementType.INDEX] = GL_ELEMENT_ARRAY_BUFFER
        }
    }

    var vboPtr = glGenBuffer(); private set

    init {
        val glType = types[type]!!

        glBindBuffer(glType, vboPtr)
        glBufferData(glType, (data.capacity * sizes[type]!!).toLong(), data.memory.pointer, GL_STATIC_DRAW)
        glBindBuffer(glType, 0u)
    }

    fun clear() {
        if (vboPtr != 0u) {
            glDeleteBuffer(vboPtr)
            vboPtr = 0u
        }
    }
}