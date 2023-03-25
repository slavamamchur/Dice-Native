package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import io.ktor.utils.io.bits.*
import io.ktor.utils.io.core.*
import io.ktor.utils.io.core.internal.*
import kotlinx.cinterop.nativeHeap

@OptIn(DangerousInternalIoApi::class)
class VBOData(val type: ElementType = ElementType.INDEX,
                val size: Int = Short.SIZE_BYTES,
                val stride: Int = 0,
                val pos: Int = 0,
                data: Buffer) {

    companion object {
        enum class ElementType { VERTEX, INDEX }

        val sizes: MutableMap<ElementType, Int> = hashMapOf(ElementType.VERTEX to Float.SIZE_BYTES,
            ElementType.INDEX to Short.SIZE_BYTES)
        val types: MutableMap<ElementType, UInt> = hashMapOf(ElementType.VERTEX to GL_ARRAY_BUFFER,
            ElementType.INDEX to GL_ELEMENT_ARRAY_BUFFER)
    }

    var vboPtr = glGenBuffer(); private set

    init {
        val glType = types[type]!!

        glBindBuffer(glType, vboPtr)
        glBufferData(glType, (data.capacity * sizes[type]!!).toLong(), data.memory.pointer, GL_STATIC_DRAW)
        glBindBuffer(glType, 0u)

        nativeHeap.free(data.memory)
    }

    fun clear() {
        if (vboPtr != 0u) {
            glDeleteBuffer(vboPtr)
            vboPtr = 0u
        }
    }
}