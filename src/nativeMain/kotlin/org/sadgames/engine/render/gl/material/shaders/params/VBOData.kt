package org.sadgames.engine.render.gl.material.shaders.params

import com.kgl.opengl.*
import io.ktor.utils.io.bits.*
import kotlinx.cinterop.nativeHeap
import kotlinx.cinterop.refTo
import org.sadgames.engine.utils.memSize

open class VBOData(val type: UInt = GL_ELEMENT_ARRAY_BUFFER,
                   val size: Int = Short.SIZE_BYTES,
                   val stride: Int = 0,
                   val pos: Int = 0) {

    var handle = glGenBuffer(); private set
    var data: Any? = null; protected set

    inline fun bind() = glBindBuffer(type, handle)
    inline fun unBind() = glBindBuffer(type, 0u)

    open fun put(data: FloatArray) {
        bind()
        glBufferData(type, data.memSize, data.refTo(0), GL_STATIC_DRAW)
        unBind()

        this.data = data
    }

    fun release() {
        unBind()
        data = null

        if (handle > 0u) {
            glDeleteBuffer(handle)
            handle = 0u
        }
    }
}